---
tags:
  - TECHNOLOGIES/PDFBox
  - TECHNOLOGIES/PDF
---
## Что такое **Apache PDFBox**?

**Apache PDFBox** — это **open-source Java-библиотека**, созданная для:

🖊️ **Чтения, редактирования и создания PDF-документов**  
📤 **Извлечения текста, шрифтов, изображений**  
🔍 **Поиска и анализа содержимого PDF**  
🔐 **Работы с метаданными, шифрованием и цифровыми подписями**

> Под капотом PDFBox использует классы, которые читают структуру PDF — а это, по сути, набор байтов, меток, потоков и объектов.

---
## 🧰 Основные **компоненты** PDFBox:

|Компонент|Назначение|
|---|---|
|`PDDocument`|Загружает и представляет PDF-файл|
|`PDPage`|Одна страница PDF|
|`PDPageContentStream`|Позволяет писать/рисовать на странице|
|`PDFTextStripper`|Извлекает текст из документа|
|`PDImageXObject`|Работа с изображениями|
|`PDField` и `PDAcroForm`|Работа с формами/поля ввода в PDF|

---
## 🧪 Примерчики (чисто, чтобы не зевать):
### 📄 Создание PDF
```java
PDDocument document = new PDDocument();
PDPage page = new PDPage();
document.addPage(page);

PDPageContentStream contentStream = new PDPageContentStream(document, page);
contentStream.beginText();
contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
contentStream.newLineAtOffset(100, 700);
contentStream.showText("Привет, мир PDFBox!");
contentStream.endText();
contentStream.close();

document.save("output.pdf");
document.close();
```

### 🔍 Извлечение текста
```java
PDDocument document = PDDocument.load(new File("example.pdf"));
PDFTextStripper stripper = new PDFTextStripper();
String text = stripper.getText(document);
System.out.println(text);
document.close();
```

---
## 🧠 Как работает “алгоритм извлечения текста”?
1. PDFBox **сканирует PDF** как поток объектов (текст, линии, картинки…)    
2. Используется `PDFTextStripper`, который:    
    - Ищет текстовые **элементы на страницах**        
    - Распознаёт **позиции** символов        
    - Собирает их в строки с учётом координат        
3. Результат — **сырой текст**, как из OCR, только без камеры 😄    

---
## ⚙️ А что под капотом?
PDFBox реализует **PDF specification (ISO 32000)**:

- работает напрямую с **COS-объектами** (PDF internal format)
    
- интерпретирует **Content Stream** — инструкции по рисованию текста, форм и картинок
    
- может даже обрабатывать **аннотации, подписи, поля форм** и **XMP-метаданные**