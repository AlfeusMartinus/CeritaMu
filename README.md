# CeritaMu

**CeritaMu** adalah aplikasi storytelling yang memungkinkan pengguna untuk mendaftar, login, mengunggah, dan berbagi cerita pribadi. Aplikasi ini dibangun menggunakan **Kotlin** dengan arsitektur **MVVM** dan dikembangkan menggunakan **Android Studio**.

![CeritaMu](./project21.png)

---

## Fitur Utama
- **Registrasi & Login:** Pengguna dapat membuat akun baru atau masuk dengan akun yang sudah ada.
- **Unggah Cerita:** Pengguna dapat menulis cerita dan mengunggahnya.
- **Bagikan Cerita:** Cerita yang diunggah dapat dibagikan kepada pengguna lain.
- **Antarmuka Modern:** Desain antarmuka yang sederhana dan responsif menggunakan prinsip **Material Design**.

---

## Teknologi yang Digunakan
- **Bahasa Pemrograman:** Kotlin
- **Arsitektur:** MVVM (Model-View-ViewModel)
- **Tools:** Android Studio
- **Library:**
  - **Retrofit:** Untuk komunikasi dengan API.
  - **Glide:** Untuk memuat dan menampilkan gambar.
  - **LiveData & ViewModel:** Untuk manajemen data secara reaktif.
  - **Hilt:** Untuk Dependency Injection.

---

## Instalasi
Ikuti langkah-langkah berikut untuk menjalankan aplikasi di lingkungan pengembangan lokal Anda:

1. **Clone repository ini:**
   ```bash
   git clone https://github.com/username/ceritamu.git
   
2. **Buka proyek di Android Studio:**
   - Pastikan Anda telah menginstal **Android Studio** versi terbaru.
   - Pilih **File > Open** di Android Studio, lalu navigasikan ke folder proyek yang sudah di-*clone*.

3. **Instal dependensi dengan Gradle:**
   Gradle secara otomatis akan mengunduh dan menginstal semua dependensi saat proyek dibuka.

4. **Jalankan aplikasi:**
   - Hubungkan perangkat Android atau jalankan emulator di Android Studio.
   - Klik tombol **Run** (ikon hijau dengan gambar segitiga) di Android Studio untuk membangun dan menjalankan aplikasi.
   - Aplikasi akan dibuka pada perangkat atau emulator yang dipilih.

---

## Struktur Proyek
Proyek ini mengikuti struktur standar Android dengan arsitektur **MVVM**. Berikut adalah gambaran umum struktur folder:

- **app/**
  - **src/**
    - **main/**
      - **java/**
        - **com.example.ceritamu/** (Nama paket aplikasi)
          - **data/**: Berisi model dan kelas data.
          - **di/**: Berisi kelas untuk Dependency Injection menggunakan Hilt.
          - **ui/**: UI komponen aplikasi.
          - **viewmodel/**: Berisi ViewModel untuk komunikasi antara UI dan data.
      - **res/**
        - **layout/**: XML layout untuk UI.
        - **drawable/**: Gambar dan aset visual lainnya.
        - **values/**: File resource seperti string, warna, dan tema.

---

## Kontribusi
Kontribusi untuk pengembangan aplikasi ini sangat diterima! Silakan buat **Pull Request** atau laporkan masalah melalui **Issues** di repository ini.

Untuk berkontribusi:
1. Fork repositori ini.
2. Buat branch untuk fitur atau perbaikan Anda.
3. Lakukan perubahan yang diinginkan dan commit.
4. Kirim Pull Request untuk review dan integrasi.

---

## Kontak
Dikembangkan oleh [Alfeus Martinus](mailto:feusmartinus@gmail.com).

---

## Lisensi
Proyek ini dilisensikan di bawah [MIT License](LICENSE).
