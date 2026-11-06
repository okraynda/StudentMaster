package com.studentmanagement;

public class Student {

    private int id;
    private String nim;
    private String nama;
    private String jenisKelamin;
    private String jurusan;
    private String email;
    private String noTelepon;
    private String foto;

    public Student() {
    }

    public Student(int id, String nim, String nama,
                   String jenisKelamin, String jurusan,
                   String email, String noTelepon, String foto) {
        this.id = id;
        this.nim = nim;
        this.nama = nama;
        this.jenisKelamin = jenisKelamin;
        this.jurusan = jurusan;
        this.email = email;
        this.noTelepon = noTelepon;
        this.foto = foto;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNim() {
        return nim;
    }

    public void setNim(String nim) {
        this.nim = nim;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getJenisKelamin() {
        return jenisKelamin;
    }

    public void setJenisKelamin(String jenisKelamin) {
        this.jenisKelamin = jenisKelamin;
    }

    public String getJurusan() {
        return jurusan;
    }

    public void setJurusan(String jurusan) {
        this.jurusan = jurusan;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNoTelepon() {
        return noTelepon;
    }

    public void setNoTelepon(String noTelepon) {
        this.noTelepon = noTelepon;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }
}