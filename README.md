# Restaurant-ManagementSystem
Stocks, Orders, Feedback and Shipping

1.	Gerçekleştirdiğiniz veri tabanı projesi için grup arkadaşlarınızın isimlerini yazınız ve projenize ait veri tabanı/diğer yazılım bileşenleri hakkında bilgi veriniz. (5 p)

Projemiz, Ahmet Önder Önlü, Furkan Enes Ergül ve Raşit Bera Topal tarafından; Java programlama dili, Mssql veri tabanı ve JavaFX (Scene Builder) arayüzü kullanılarak gerçekleştirilmiştir. 


2.	Gerçekleştirdiğiniz veri tabanı projesi için proje dokümanınızı ve dosyalarınızı içeren herkese açık github bağlantılarınızı paylaşınız. (5 p)

3.	Gerçekleştirdiğiniz projenin amacını detaylı bir şekilde açıklayınız. (10 p)
Bu projenin amacı, restoranların günlük operasyonlarını daha verimli bir şekilde yönetmelerine olanak sağlayan bir sipariş ve stok yönetim sistemi geliştirmektir. Sistem, müşteri siparişlerinin, yemeklerin, kullanılan malzemelerin ve mevcut stok durumunun takibini yaparak, işletme süreçlerini optimize etmeyi hedefler.
Projenin sunduğu ana işlevler şunlardır:
1. Müşteri Yönetimi: Müşteri bilgilerini kaydetme ve düzenleme.
2. Sipariş Takibi: Alınan siparişleri kaydederek ödeme miktarlarını ve sipariştarihlerini yönetme.
3. Yemek Yönetimi: Menüdeki yemeklerin ve tariflerin düzenlenmesi.
4. Stok Yönetimi: Malzeme stoklarının izlenmesi, güncellenmesi ve stok seviyelerinin kritik seviyelerde uyarı vermesi.
5. Malzeme Kullanım Takibi: Her bir yemek için gereken malzemelerin miktarlarının belirlenmesi ve stoktan otomatik düşülmesi.
Bu sistem, restoranların günlük iş yükünü azaltmak, hata oranını düşürmek, ve işsüreçlerini hızlandırmak için kapsamlı bir çözüm sunmaktadır.
4.	Tasarladığınız veri tabanı mimarisinde hangi tablo ve ilişkileri kullanıldığınızı açıklayınız. (10 p)

Tablolar birbirlerine foreign ve primary key ile methodu cascade ve null olacak şekilde bağlanmıştır.

Customers (Müşteriler):
- CustomerID: Her müşteri için benzersiz id numarası.
- Name: Müşteri adı.
- Phone: Müşteri telefon numarası.
- Email: Müşterinin e-posta adresi.
Müşterilere ait bilgileri tutar.

Orders (Siparişler):
- OrderID: Her sipariş için benzersiz bir kimlik numarası.
- CustomerID: Siparişi veren müşterinin yabancı kimliği (Customers tablosuna
bağlanır).
- OrderDate: Sipariş tarihi.
- TotalAmount: Siparişin toplam tutarı.
Müşteri tarafından verilen siparişlerin kayıtlarını tutar.

OrderItems (Sipariş Ögeleri):
- OrderItemID: Her sipariş ögesi için benzersiz bir kimlik numarası.
- OrderID: Siparişin kimliği (Orders tablosuna bağlanır).
- DishID: Sipariş edilen yemeğin kimliği (Dishes tablosuna bağlanır).
- Quantity: Sipariş edilen miktar.
- Price: Sipariş edilen miktarın toplam fiyatı.
Sipariş içinde hangi yemeklerin ne kadar sipariş edildiğini detaylandırır.

Dishes (Yemekler):
- DishID: Her yemek için benzersiz bir kimlik numarası.
- DishName: Yemeğin adı.
- Price: Yemeğin birim fiyatı.
Restoran menüsündeki yemeklerin bilgilerini tutar.

DishIngredients (Yemek Malzemeleri):
- DishID: Yemeğin kimliği (Dishes tablosuna bağlanır).
- IngredientID: Yemeğin yapımında kullanılan malzemenin kimliği (Ingredients
tablosuna bağlanır).
- QuantityNeeded: Yemeği yapmak için gereken malzeme miktarı.
Her yemeğin hangi malzemeleri ve ne kadar miktarda gerektirdiğini belirtir.

Ingredients (Malzemeler):
- IngredientID: Her malzeme için benzersiz bir kimlik numarası.
- IngredientName: Malzemenin adı.
- Unit: Malzemenin ölçü birimi (ör. gram, litre).

Restoranın kullandığı tüm malzemelerin listesini tutar.

Stock (Stok):
- IngredientID: Malzemenin kimliği (Ingredients tablosuna bağlanır).
- QuantityInStock: Stokta bulunan malzeme miktarı. Her malzemenin mevcut stok durumunu takip eder.

Tüm tabloların görevleri, alanların açıklamaları yapıldıktan sonra belirtilmiştir. Tablolara birbirlerine birincil ve yabancı anahtar ile bağlanmış; böylece hangi müşteri hangi siparişleri vermiş, tutarı ne kadar ve bu sipariş sonucu stock malzemeleri arasındaki bağlantılar sağlanmıştır.
5.	Veri tabanı ER (Entity Relationship) diagramının bilgisayar ortamında çizilmiş halini paylaşınız. (Ara raporda eksik kısımlar bu raporda giderilmelidir ve ER çizme programlarından faydalanıbilir. Elle çizim, çizip fotoğrafını çekme vb. kabul edilmeyecektir.) (10 p)

6.	Herhangi iki tablonuz için DDL (create) kodları yazılmalıdır. (10 p)

Orders tablosu için CREATE DDL sorgusu
CREATE TABLE Orders (
OrderID INT PRIMARY KEY,       -- Birincil anahtar
CustomerID INT NOT NULL,       -- Foreign key için gerekli
OrderDate DATETIME NOT NULL,   -- Sipariş tarihi
TotalAmount DECIMAL(10, 2) NOT NULL -- Toplam tutar
);

Customers tablosu için CREATE DDL sorgusu
CREATE TABLE Customers (
CustomerID INT PRIMARY KEY,    -- Birincil anahtar
Name NVARCHAR(100) NOT NULL,   -- Müşteri adı
Phone NVARCHAR(15),            -- Telefon numarası
Email NVARCHAR(100)            -- Email adresi);

7.	5 adet DML (update, insert, delete) içeren kodları yazılmalıdır. (10 p)
INSERT INTO Customers (CustomerID, Name, Phone, Email)
VALUES (1, 'Ahmet Önder', '5551234567', 'ahmet.onder@example.com');

INSERT INTO Orders (OrderID, CustomerID, OrderDate, TotalAmount)
VALUES (101, 1, '2024-12-25', 150.50);

UPDATE Customers
SET Phone = '5559876543'
WHERE CustomerID = 1;

UPDATE Customers
SET Email = 'ahmet.updated@example.com'
WHERE CustomerID = 1;

DELETE FROM Customers
WHERE CustomerID = 2;
 
8.	Projenize ait kendi belirlediğiniz 10 adet SQL sorgusu yazınız, sorguların amacını ve sonuç çıktısını da lütfen ekleyiniz.  (Açıklama: Sorgular Select deyimleri ve gruplama fonksiyonlarını HAVING deyimini (min, max, avg, count gibi) ve join deyimlerini (en az iki tablo ile birleştirme sorgusu) içerecek şekilde basitten karmaşığa doğru gitmelidir. Proje sunum anında veri tabanınıza ait sorular SQL ortamında gösterilecek ve açıklanacaktır. Raporunuzda ise sorgular, sorguların cevap ve sonuçlarının ekran görüntüsü olarak paylaşılması beklenmektedir. (10 p) 

Amaç: Customers tablosundaki tüm kayıtları getirir.
Sonuç: Müşteri ID, isim, telefon ve e-posta bilgilerini listeleyen bir tablo.
SELECT * FROM Customers;

Amaç: CustomerID = 1 olan müşterinin siparişlerini listelemek.
      Sonuç: Müşteri ID'si 1 olan kişinin sipariş numaraları, tarihleri ve toplam tutarları.
SELECT O.OrderID, O.OrderDate, O.TotalAmount 
FROM Orders O WHERE O.CustomerID = 1;

Amaç: Orders tablosundaki toplam sipariş sayısını öğrenmek.
Sonuç: Veritabanında kaç adet sipariş olduğunu gösteren bir sayı.
SELECT COUNT(*) AS TotalOrders
FROM Orders;

Amaç: Stock tablosundaki tüm malzemelerin mevcut stok bilgilerini listelemek.
Sonuç: Malzeme ID'si, malzeme adı ve stoktaki miktar bilgisi.
SELECT S.IngredientID, I.IngredientName, S.QuantityInStock 
FROM Stock S
JOIN Ingredients I ON S.IngredientID = I.IngredientID;

Amaç: DishID = 1 olan yemeğin hangi malzemelerle yapıldığını ve miktarlarını listelemek.
Sonuç: Yemeğin adı, kullanılan malzemelerin isimleri ve gereken miktar bilgisi.
SELECT D.DishName, I.IngredientName, DI.QuantityNeeded 
FROM Dishes D
JOIN DishIngredients DI ON D.DishID = DI.DishID
JOIN Ingredients I ON DI.IngredientID = I.IngredientID
WHERE D.DishID = 1;

Amaç: Orders tablosundaki tüm siparişlerin toplam tutarını hesaplar.
Sonuç: Tüm siparişlerin toplam tutarı (gelir).
SELECT SUM(TotalAmount) AS TotalRevenue
FROM Orders;

Amaç: Her müşteri için toplam sipariş sayısını listelemek.
Sonuç: Her müşterinin adı ve yaptığı toplam sipariş sayısı.
SELECT C.CustomerID, C.Name, COUNT(O.OrderID) AS TotalOrders
FROM Customers C
LEFT JOIN Orders O ON C.CustomerID = O.CustomerID
GROUP BY C.CustomerID, C.Name;

Amaç: OrderItems tablosundaki tüm ürünlerin toplam miktarını ve ortalama fiyatını hesaplar.
Sonuç: Siparişlerde kullanılan toplam ürün miktarı ve ürünlerin ortalama fiyatı.
SELECT SUM(Quantity) AS TotalQuantity, AVG(Price) AS AveragePrice
FROM OrderItems;

Amaç: Stokta miktarı 10’dan az olan malzemeleri bulmak.
Sonuç: Azalan malzemelerin isimleri ve mevcut stok miktarları.
SELECT I.IngredientName, S.QuantityInStock 
FROM Stock S
JOIN Ingredients I ON S.IngredientID = I.IngredientID
WHERE S.QuantityInStock < 10;


Amaç: En fazla sipariş yapan müşterinin bilgilerini getirir.
Sonuç: En fazla sipariş veren müşterinin adı, ID'si ve toplam sipariş sayısı.
SELECT TOP 1 C.CustomerID, C.Name, COUNT(O.OrderID) AS TotalOrders
FROM Customers C
JOIN Orders O ON C.CustomerID = O.CustomerID
GROUP BY C.CustomerID, C.Name
ORDER BY TotalOrders DESC;


9.	Eğer gerçekleştirmiş iseniz, veri tabanı bağlama ve uygulama geliştirme aşamalarınızı kısaca açıklayarak, kullanıcı ara yüz ekranından bir örnek veriniz. Ve geliştirdiğiniz ara yüzü anlatınız. (10 p)

Veri tabanına mssql configuration manager üzerinden TCP/IP ve diğer haberleşme protokollerini aktifleştirdim ve bu şekilde sa yönetici ve password’ü kullanarak bağlantı sağladım. Daha sonra maven dosyası üzerine mssql için bağıntıları ekledik. Böylece bağlantıyı başarıyla sağladık. Daha sonra Java üzerinden oluşturulan sql sorguları aracılığyla veri tabanı üzerinde ddl ve dml komutlarını çalıştırmayı başardık. 
Kullanıcı arayüzünde ilk başta kullanıcı girişi yapılması istenmekte ve daha sonrasında bu kullanıcının id’si kullanılarak bunun üzerinden işlemler yapılmaktadır. Daha sonraki menü sayfasında 4 farklı sekme bulunmakta bunlar müşteri işlemleri ve yönetim işlemlerini yönetmektedir. Buradan açılan sayfalar üzerinde yapılan işlemler veri tabanını etkilemektedir.

 

10.	Eğer veri tabanı bağlama işlemini gerçekleştirmemiş iseniz VTYS sistemlerinde Transaction nedir açıklayınız ve çalışmanızdan bir Transaction örneği veriniz. (10 p)

Veri tabanı bağlama işlemi gerçekleştirilmiştir.


11.	View nedir açıklayınız ve bir adet view, bir adet saklı yordam (Stored Procedute)  ifadesine ait SQL deyimlerinin sorgusunu ve cevabını yazınız.
(10 p)

View (görünüm), bir veya birden fazla tablodan alınan verilerin SQL sorgusu ile bir araya getirilerek sanal bir tablo oluşturulmasıdır. Bu sanal tablo fiziksel olarak veri saklamaz, yalnızca sorgu sonuçlarını görüntüler. Veri tabanı üzerindeki karmaşık sorguları sadeleştirmek ve güvenliği artırmak için kullanılır.

Stored Procedure (saklı yordam), bir veya birden fazla SQL sorgusunun bir araya getirilerek bir blok halinde saklanmasıdır. Bu yordamlar, tekrar eden işlemler için kullanılır ve parametre alarak dinamik bir şekilde çalışabilir.

CREATE VIEW CustomerOrdersView AS
SELECT 
    C.CustomerID,
    C.Name AS CustomerName,
    C.Email,
    O.OrderID,
    O.TotalAmount
FROM Customers C
LEFT JOIN Orders O ON C.CustomerID = O.CustomerID;
SELECT * FROM CustomerOrdersView;

