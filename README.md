# Tubes_bless
Tugas Besar 1 IF2211 Strategi Algoritma
# Tubes1_Bless
Tugas Besar I IF2211 Strategi Algoritma Semester II Tahun 2022/2023 Pemanfaatan Algoritma Greedy dalam Aplikasi Permainan "Galaxio"

## Daftar Isi
* [Deskripsi Singkat Program](#deskripsi-singkat-tugas)
* [Strategi Greedy Program](#strategi-greedy-program)
* [Requirement Program](#requirement-program)
* [Cara Kompilasi Program](#cara-kompilasi-program)
* [Cara Menjalankan Program](#cara-menjalankan-program)
* [Alternatif Lain Cara Menjalankan Program](#alternatif-lain-cara-menjalankan-program)
* [Author Program](#author-program)

## Deskripsi Singkat Tugas
Galaxio adalah sebuah game battle royale yang mempertandingkan beberapa bot kapal. Setiap pemain akan memiliki sebuah bot kapal dan tujuan dari permainan adalah agar bot kapal tetap hidup hingga akhir permainan sehingga dapat memenangkan pertandingan, setiap bot harus mengimplementasikan strategi tertentu untuk dapat memenangkan permainan
Bahasa pemrograman yang digunakan pada tugas besar ini adalah Java. Bahasa Java tersebut digunakan untuk membuat algoritma pada bot.Untuk menjalankan permainan, digunakan sebuah game engine yang diciptakan oleh Entellect Challenge yang terdapat pada repository githubnya. 

## Strategi Greedy Program
Dalam permainan Galaxio, tujuan setiap *bot* pemain berusaha untuk tetap hidup. 
Terdapat banyak cara untuk meraih hal tersebut, seperti mencoba menabrak bot yang ukurannya lebih kecil, menghindari bot yang ukurannya lebih 
besar dan sebagainya secara lebih rinci dijelaskan di bawah ini. 

### Strategi Menghindari dan Mengejar musuh
Seperti yang telah diketahui sebelumnya, bot akan memenangkan pertandingan ketika scorenya lebih besar dan dapat bertahan hingga akhir, adapun ketika ukuran bot mengecil dan kurang dari 5 maka bot akan musnah. Untuk itu ketika bot yang dibuat ingin menang ukuran dari bot harus dipertahankan.Oleh karena itu, dibutuhkan algoritma *greedy* yang menangani *musuh* secara efektif dan efisien.

### Strategi Bot Tidak Keluar Arena
Hal lain yang harus diperhatikan agar bot memenangkan sebuah pertandingan adalah menjaga bot agar tetap di arena permainan. Untuk ini bot harus
mampu menjaga jarak dengan batas luar permainan. Algoritma greedy untuk ini juga dibutuhkan. 

### Menjauhi Gas cloud
Salah satu bagian lain yang harus dihindari adalah gas cloud. Gas cloud dapat membuat ukuran bot berkurang 1 setiap 1 tick. Hal ini cukup merugikan bot untuk itu menghindari gas cloud adalah hal yang penting untuk dilakukan.Algoritma greedy juga cukup dibutuhkan disini

### Strategi pada Permasalahan AfterBurner
Afterburner dapat membuat kecepatan bot dua kali lebih cepat dibanding kecepatan aslinya. Hal ini dapat dimanfaatkan untuk mengejar musuh yang lebih kecil. 

### Strategi pada Permasalahan Torpedo Salvo Shoot (Menembak Musuh)
Strategi berikutnya adalah strategi untuk menembak musuh. Torpedo Salvo Shoot merupakan salah satu fitur yang ada pada game galaxio. Fitur ini membuat kapal satu dengan yang lainnya dapat menembakkan sebuah tembakan. Tembakan ini dapat membuat ukuran kapal yang terkena tembakkan menjadi berkurang ukurannya dan kapal yang menembakkan akan bertambah ukurannya. Karena fitur ini cukup menguntungkan diperlukan juga algoritma greedy nya

### Strategi Mencari/Mengambil Food atau SuperFood
Untuk menambah ukuran bot tidak hanya harus menabrak kapal musuh tapi bisa dengan mengambil food yang ada di arena permain.Food dan superfood ini
cukup mempunyai peran penting untuk kemenangan bot sehingga perlu diimplementasikan algoritma greedy nya

### Strategi Permasalahan Penggunaan Wormhole
Objek lain yang menjadi bagian dari game galaxio adalah wormhole. Wormhole ada secara berpasangan dan memperbolehkan kapal dari player untuk memasukinya dan keluar di pasangan satu lagi. Wormhole dapat menjadi suatu fitur unik untuk memenangkan pertandingan untuk itu fitur ini
perlu diimplementasikan juga greedy nya.

### Strategi Permasalahan Pengambilan dan Penggunaan Supernova
Senjata yang paling ampuh pada game galaxio ini adalah Supernova. Supernova dapat menghancurkan beberapa bot kapal musuh dalam satu waktu. Supernova memberikan sebuah efek ledakan yang dapat membuat semua kapal musuh di area itu hancur.Keunikan yang ada pada fitur ini juga harus
diperhitungkan dan dibuat algoritma greedy nya. 

### Strategi Penggunaan Teleport
Player dapat memanfaatkan fitur teleport untuk memindahkan bot ke suatu titik dan dengan arah yang dituju pada peta. Fitur ini juga cukup 
menguntungkan sehingga perlu diimplementasikan algoritma greedy nya 

## Requirement Program
* Java Virtual Machine (JVM) versi 11 atau lebih baru.
* IntelliJ IDEA versi 2021.3 atau lebih baru.
* Apache Maven 3.8.4
* NodeJS
* .Net Core 3.1

## Cara Kompilasi Program
* Download file `starter-pack.zip` pada link [berikut](https://github.com/EntelectChallenge/2021-Galaxio/releases/tag/2021.3.2).
* Unzip file `starter-pack.zip` pada mesin eksekusi.
* Lakukan cloning repository ini sebagai folder ke dalam folder `starter-pack`.
* Buka IDE IntelliJ IDEA pada mesin eksekusi.
* Seharusnya, Apache Maven sudah terinstall di dalam IntelliJ IDEA. Jika Maven belum ada, Anda dapat mendownloadnya pada link [berikut](https://maven.apache.org/download.cgi).
* Klik kanan pada file `pom.xml`, kemudian pilih perintah `Add to Maven`.
* Kemudian, jalankan perintah built `compile` dan `install` pada Apache Maven.
* Bila terdapat file `.jar` baru pada folder `target`, maka program berhasil dikompilasi.
* Anda dapat mengubah directory hasil kompilasi Apache Maven dengan menggunakan file konfigurasi `bot.json`. Untuk penjelasan mengenai file konfigurasi, Anda dapat mengakses laporan pada folder `doc`.

## Cara Menjalankan Program
* Lakukan konfigurasi jumlah bot yang ingin dimainkan pada file JSON ”appsettings.json” dalam folder “runner-publish” dan “engine-publish”
* Buka terminal baru pada folder runner-publish.
* Jalankan runner menggunakan perintah “dotnet GameRunner.dll”
* Buka terminal baru pada folder engine-publish
* Jalankan engine menggunakan perintah “dotnet Engine.dll”
* Buka terminal baru pada folder logger-publish
* Jalankan engine menggunakan perintah “dotnet Logger.dll”
* Jika ingin menggunakan reference bot maka bisa gunakan perintah ”dotnet ReferenceBot.dll” namun jika ingin menggunakan bot buatan maka jar    terlebih dahulu bot buatan dengan maven atau intelejID. Kemudian jika menggunakan jar maka perintah yang ditulis adalah java -jar <path-name-jar>.
* Setelah permainan selesai, riwayat permainan akan tersimpan pada 2 file JSON “GameStateLog_{Timestamp}” dalam folder “logger-publish”. Kedua file tersebut diantaranya GameComplete (hasil akhir dari permainan) dan proses dalam permainan tersebut.
* GameStateLog yang dihasilkan dapat diinputkan ke visualizer untuk melihat permainan dengan lebih jelas, karena jika menggunakan terminal kurang bisa dipahami.

## Alternatif lain cara menjalankan Program
* Untuk menjalankan Game Runner, Engine, atau Logger pada UNIX-based OS dapat memodifikasi atau langsung menjalankan “run.sh” yang tersedia pada starter-pack. 
* Pada windows dapat menggunakan atau memodifikasi batch script seperti terdapat pada link ini https://docs.google.com/document/d/1Iws2REmb2SkopP4pYmaNVGWE7Ft6K9KSps10ZuV9yFw/edit?usp=sharing


## Author Program
* [Shelma Salsabila - 13521115]
* [Akhmad Setiawan - 13521164]
* [Satria Octavianus Nababan - 13521168]
