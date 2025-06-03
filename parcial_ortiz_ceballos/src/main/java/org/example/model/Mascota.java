package org.example.model;

import java.time.LocalDate;

public class Mascota {
  private int mascotaId;
  private String nombre;
  private String especie;
  private LocalDate fechaNacimiento;
  private int duenioId;

  public Mascota() {
  }

  public Mascota(int mascotaId, String nombre, String especie, LocalDate fechaNacimiento, int duenioId) {
    this.mascotaId = mascotaId;
    this.nombre = nombre;
    this.especie = especie;
    this.fechaNacimiento = fechaNacimiento;
    this.duenioId = duenioId;
  }

  public int getMascotaId() {
    return mascotaId;
  }

  public void setMascotaId(int mascotaId) {
    this.mascotaId = mascotaId;
  }

  public String getNombre() {
    return nombre;
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  public String getEspecie() {
    return especie;
  }

  public void setEspecie(String especie) {
    this.especie = especie;
  }

  public LocalDate getFechaNacimiento() {
    return fechaNacimiento;
  }

  public void setFechaNacimiento(LocalDate fechaNacimiento) {
    this.fechaNacimiento = fechaNacimiento;
  }

  public int getDuenioId() {
    return duenioId;
  }

  public void setDuenioId(int duenioId) {
    this.duenioId = duenioId;
  }

  @Override
  public String toString() {
    return "ID: " + mascotaId +
            " - Nombre: " + nombre +
            " - Especie: " + especie +
            (fechaNacimiento != null ? " - Nacido: " + fechaNacimiento : "");
  }

}

