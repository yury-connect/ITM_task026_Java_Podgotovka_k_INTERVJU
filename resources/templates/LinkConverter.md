<%*
const selection = tp.file.selection();

if (!selection) {
  tR = "❗ Выделите ссылку в формате [[ИмяФайла]]";
} else {
  const match = selection.match(/\[\[(.*?)\]\]/);
  if (!match) {
    tR = "❗ Формат не распознан.";
  } else {
    const fileName = match[1];
    const targetFile = app.vault.getFiles().find(f => f.basename === fileName);

    if (!targetFile) {
      tR = `❗ Файл "${fileName}" не найден.`;
    } else {
      // Получаем абсолютный путь от корня vault-а
      const fullPath = targetFile.path;  // без изменений
      tR = `[${fileName}](${fullPath})`;
    }
  }
}
%>
