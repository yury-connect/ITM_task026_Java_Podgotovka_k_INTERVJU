---
tags:
  - TECHNOLOGIES/Tesseract
---

## 🔍 Что такое **Tesseract**?

**Tesseract OCR** — это **библиотека для распознавания текста с изображений**, т.е. технология **OCR (Optical Character Recognition)**.  
Она позволяет "считать" текст с:
- отсканированных документов,    
- фотографий,    
- скриншотов,    
- PDF-файлов, содержащих изображения.    

Библиотека разработана Google и является **бесплатной и с открытым исходным кодом**.

---
## 🧠 Как работает OCR с Tesseract?
Пример:
- У вас есть скан договора в формате JPG или PNG.    
- На изображении написано:
```makefile
Договор №1243 
Дата: 12.01.2024 
Арендодатель: ООО "Ромашка"
```
- Вы подаете это изображение в Tesseract — и он возвращает этот текст в виде строки.    

---
## 🛠️ Как использовать Tesseract в Java-проекте?
Для интеграции с Java обычно используется библиотека-обертка **Tess4J**.
### 1. **Установка Tesseract (движка)**

На сервере или машине, где работает приложение, должен быть установлен Tesseract:
	Пример для Ubuntu:
```bash
sudo apt install tesseract-ocr
```
### 2. **Добавление Tess4J в проект (Maven)**
```xml
<dependency>
  <groupId>net.sourceforge.tess4j</groupId>
  <artifactId>tess4j</artifactId>
  <version>5.4.0</version>
</dependency>
```
Версия зависимости - с сайта [mvnrepository.com](https://mvnrepository.com/artifact/net.sourceforge.tess4j/tess4j/4.5.1)    (Tess4J Tesseract For Java » 4.5.1)
Версия зависимости - с сайта [central.sonatype.com](https://central.sonatype.com/artifact/net.sourceforge.tess4j/tess4j)
### 3. **Простой пример кода:**
```java
import net.sourceforge.tess4j.Tesseract;
import java.io.File;

public class OcrExample {
    public static void main(String[] args) {
        Tesseract tesseract = new Tesseract();
        // Указать путь к языковым моделям, например: /usr/share/tesseract-ocr/4.00/tessdata/
        tesseract.setDatapath("/path/to/tessdata");
        // Указать язык — для русского: rus, для английского: eng, можно комбинировать: "rus+eng"
        tesseract.setLanguage("rus");
        try {
            String result = tesseract.doOCR(new File("contract_scan.png"));
            System.out.println("Распознанный текст:");
            System.out.println(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```

---
## 🧰 Где Tesseract применим в реальных задачах?
В случае из вашего резюме — Tesseract использовался для:
- **распознавания данных из сканов договоров**;    
- **автоматического извлечения информации** (номер, дата, стороны, суммы и т.д.);    
- **предзаполнения форм** — пользователь загружает скан, а система уже показывает готовые поля;    
- **автоматической проверки/валидации** документов.    

---
## ⚠️ Особенности и ограничения
- Качество распознавания сильно зависит от **качества изображения**.    
- Поддерживает **много языков**, включая русский (но русские шрифты требуют хорошей настройки).    
- Иногда требуется **предобработка изображений** (обрезка, увеличение контрастности, удаление шумов).    

---
Если нужно, могу также показать, как сделать **предобработку изображения** перед OCR или как использовать Tesseract в связке с Spring Boot REST-сервисом.