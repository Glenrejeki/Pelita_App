


# 🕯️ Pelita App

**Pelita App** adalah aplikasi Android berbasis **Jetpack Compose** untuk berbagi firman Tuhan, renungan, dan pesan rohani secara publik—terinspirasi dari model *thread-based social platform*.

Aplikasi ini dirancang dengan arsitektur modern Android, mendukung **tema terang & gelap**, serta siap digunakan sebagai aplikasi nyata dengan backend online.

---

## ✨ Fitur Utama

### 📖 Berbagi Firman & Renungan
- Membuat postingan teks (firman Tuhan / renungan)
- Feed bergaya *thread*
- Detail postingan & komentar

### 🔁 Repost (Share Ulang)
- Repost postingan pengguna lain
- (Planned) Quote repost dengan pesan tambahan

### ❤️ Interaksi Sosial
- Like & komentar
- Follow / unfollow pengguna
- Profil pengguna (bio, postingan)

### 🔍 Pencarian
- Cari akun
- Cari postingan / firman

### ⚙️ Pengaturan
- Edit profil
- Ganti password
- Toggle **tema terang / gelap**
- Logout & delete account

---

## 🛠️ Teknologi yang Digunakan

### Android
- **Kotlin**
- **Jetpack Compose**
- **Material 3**
- **Navigation Compose**
- **MVVM Architecture**
- **StateFlow**
- **DataStore** (theme preference)
- **Hilt** (Dependency Injection)

### Backend (Planned / Integrated)
- **Supabase**
  - Authentication
  - PostgreSQL database
  - Storage (avatar)
  - Row Level Security (RLS)

---

## 🗂️ Struktur Proyek


```md

pelita-app/
├─ android/          # Android application (Jetpack Compose)
├─ supabase/         # SQL schema, RLS policies, migrations
├─ docs/             # Documentation & architecture notes
└─ README.md

```

### Struktur Android (ringkas)
```

com.example.pelitaapp
├─ core/        # theme, navigation, data, utils
├─ feature/     # auth, feed, post, profile, search, settings
├─ di/          # dependency injection
├─ MainActivity.kt
└─ PelitaApp.kt

```

---

## 🧱 Arsitektur

Aplikasi ini menggunakan pendekatan **Clean-ish MVVM**:

```

UI (Compose)
↓
ViewModel (StateFlow)
↓
UseCase (optional)
↓
Repository
↓
Remote Data Source (Supabase)

````

---

## 🌗 Tema Terang & Gelap

Pelita App mendukung **Light & Dark Theme**:
- Menggunakan Material 3
- Preferensi tema disimpan menggunakan **DataStore**
- Otomatis bertahan walaupun aplikasi ditutup

---

## 🚀 Cara Menjalankan Proyek

### Prasyarat
- Android Studio Hedgehog / Iguana atau lebih baru
- JDK 17
- Android SDK API 36

### Langkah
1. Clone repository:
   ```bash
   git clone https://github.com/username/pelita-app.git
    ``
  ``
2. Buka folder `android/` di Android Studio
3. Pastikan **Gradle JDK = 17**
4. Sync Gradle
5. Run di emulator / device

---

## 🗄️ Database (Supabase)

Struktur utama database:

* `profiles`
* `posts` (original / repost / quote)
* `comments`
* `likes`
* `follows`
* `bookmarks`
* `reports`

SQL schema & RLS policy tersedia di folder:

```
supabase/migrations/
```

---

## 🛣️ Roadmap

### MVP (In Progress)

* [x] Setup Compose & Theme
* [x] Navigation & UI skeleton
* [ ] Authentication
* [ ] Feed & Create Post
* [ ] Like & Comment
* [ ] Repost

### Next

* [ ] Quote repost
* [ ] Bookmark
* [ ] Notification
* [ ] Moderation (report & block)
* [ ] Offline cache (Room)


---

## 🙏 Penutup

> *“Firman-Mu itu pelita bagi kakiku dan terang bagi jalanku.”*
> — Mazmur 119:105

Pelita App dibuat sebagai ruang digital untuk saling menguatkan melalui firman Tuhan dan refleksi iman.

---

✨ Dibuat dengan ❤️ menggunakan Kotlin & Jetpack Compose

```


