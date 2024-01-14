![labreportingapp.png](src%2Fmain%2Fresources%2Flabreportingapp.png)
![Uml Diagram.png](src%2Fmain%2Fresources%2FUml%20Diagram.png)

Laboratuvar Rapor Uygulaması

Bu proje, iki farklı kullanıcı rolü olan "User" ve "Admin" için bir laboratuvar rapor uygulamasını içermektedir.
Uygulamada Laborant kullanıcılar Admin rolüne, Patient kullanıcıları ise User rolüne sahiptir.
Aşağıda her iki kullanıcı rolünün yetenekleri ve projenin kurulumu hakkında bilgiler bulunmaktadır.

### 1-Admin/Yönetici Rolü

* :notebook: Laboratuvar Raporları İşlemleri :
  Adminler, kendi oluşturdukları raporları listeleyebilir ve tarih sırasına göre sıralayabilir,
  İsim-soyisim veya dosya numarasına göre arama yapabilir, raporları silebilir, güncelleyebilir ve Patient, rapor için
  istekte bulunduğu takdirde yeni raporlar oluşturabilir.
  Her Laborant kendi oluşturduğu raporları görüntüleyebilir ve işlem yapabilir.

### 2- User/Kullanıcı Rolü

* :notebook: Patient (User), kendi raporlarını listeleyebilir, dosya numarasına göre arama yapabilir ve yeni bir rapor
  için istekte bulunabilir.
  Laborant rapor isteğini onayladıktan sonra yeni bir istekte bulunulabilir.
  User uygulamaya kayıt olabilir.Kayıt olunmasıyla birlikte ilk rapor isteği otomatik gerçekleşir.

:computer: Projenin kurulumu

Proje'yi çalıştırmak için aşağıdaki adımları takip edebilirsiniz:

Projeyi çalıştırmak için JAVA version 17 yüklü olmalı.

## Backend

1. Projeyi indirin

```bash
git clone https://github.com/Emin-Tufan/lab-reporting-app.git
```

2. Uygulama dizinine girin `lab-reporting-app-backend`
   ```bash
   cd lab-reporting-app-backend
   ```

3. Spring boot uygulamasını başlatın:
   ```bash
   ./mvnw spring-boot:run
   ```

## FrontEnd

1. Projeyi indirin

```bash
git clone https://github.com/Emin-Tufan/lab-reporting-app-frontend.git
```

2. Uygulama dizinine girin `lab-reporting-app-frontend`
   ```bash
   cd lab-reporting-app-frontend
   ```

3. Bağımlılıkları indirin:
   ```bash
   npm install
   ```

4. Uygulamayı başlatın:
   ```bash
   npm start
   ```

Uygulama çalıştığında login olmak için uygulamaya kayıtlı bazı kullanıcıların username ve password bilgileri aşağıda
bulunmaktadır.

### Laborant Kullanıcılar

    Kullanıcı Adı: ali_y@example.com
    Şifre: parola123

    Kullanıcı Adı: selin_k@example.com
    Şifre: parola123

### Patient Kullanıcılar

    Kullanıcı Adı: burak_s@example.com
    Şifre: parola123

    Kullanıcı Adı: elif_y@example.com
    Şifre: parola789

Bu adımları takip ettikten sonra, projeniz başarıyla çalışacaktır. Her iki kullanıcı rolü için de giriş bilgilerini
kullanarak uygulamaya erişebilir ve ilgili işlemleri gerçekleştirebilirsiniz.