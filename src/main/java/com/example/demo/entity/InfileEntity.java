package com.example.demo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

@Entity(name = "InfileEntity")
@Table(name = "infile_entity")
@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
public class InfileEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column private String column1;
  @Column private String column2;
}
