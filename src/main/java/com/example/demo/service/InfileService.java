package com.example.demo.service;

import com.example.demo.entity.InfileEntity;
import lombok.RequiredArgsConstructor;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InfileService {
  private final DataSource dataSource;

  public void insertWithInfile() {
    VelocityEngine velocityEngine = new VelocityEngine();
    velocityEngine.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
    velocityEngine.setProperty(
        RuntimeConstants.RUNTIME_LOG_LOGSYSTEM_CLASS,
        "org.apache.velocity.runtime.log.Log4JLogChute");
    velocityEngine.setProperty(
        "classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
    velocityEngine.init();

    Template mapperTemplate = velocityEngine.getTemplate("/templates/testTemplate.template");

    List<InfileEntity> infileEntityList = new ArrayList<>();
    infileEntityList.add(new InfileEntity(1L, "emir", "celik"));
    infileEntityList.add(new InfileEntity(2L, "meltem", "celik"));
    infileEntityList.add(new InfileEntity(3L, "volkan", "demir"));
    infileEntityList.add(new InfileEntity(4L, "efe", "demir"));

    VelocityContext velocityContext = new VelocityContext();
    velocityContext.put("infileEntities", infileEntityList);

    Path csvPath = null;
    try {
      csvPath = Files.createTempFile("cdfgw", ".csv");
      File uploadFilePath = csvPath.toFile();
      try {
        String fileAbsolutePath = uploadFilePath.getAbsolutePath();
        generateFileWithVelocity(velocityContext, mapperTemplate, fileAbsolutePath);

        final String query =
            String.format(
                "LOAD DATA LOCAL INFILE '%s' "
                    + "IGNORE INTO TABLE infile_entity "
                    + "FIELDS TERMINATED BY ',' "
                    + "ENCLOSED BY '\"' "
                    + "LINES TERMINATED BY '%s' "
                    + "(id, column1, column2);",
                fileAbsolutePath.replace("\\", "/"), System.lineSeparator());
        try (Connection connection = DataSourceUtils.getConnection(dataSource)) {
          connection.prepareStatement(query).execute();
        }
      } finally {
        // uploadFilePath.delete();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void generateFileWithVelocity(VelocityContext context, Template template, String filePath)
      throws IOException {
    File targetFile = new File(filePath);
    if (targetFile.exists()) targetFile.delete();

    try (Writer writer = new FileWriter(targetFile)) {
      template.merge(context, writer);
    }
  }
}
