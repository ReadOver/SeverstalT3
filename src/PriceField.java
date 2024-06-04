import javax.swing.*;
import java.awt.event.*;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

public class PriceField extends JTextField {
    private static final int MAX_DECIMAL_PLACES = 2;

    public PriceField() {
        super();
        // Добавляем слушателя для обработки событий ввода
        ((AbstractDocument) getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override
            public void insertString(FilterBypass fb, int offset, String string, AttributeSet a) throws BadLocationException {
                if (!isValidPrice(string)) {
                    return; // Если текст не соответствует требованиям, запрещаем ввод
                }
                super.insertString(fb, offset, string, a);
            }

            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                if (!isValidPrice(text)) {
                    return;
                } else {
                    super.replace(fb, offset, length, text, attrs);
                }
            }
        });
    }

    /**
     * Проверяет, соответствует ли текст в поле требованиям
     */
    private boolean isValidPrice(String text) {
        if (text == null || text.isEmpty()) {
            return false;
        }


        try {
            // Преобразуем текст в число
            double price = Double.parseDouble(text);

            if (price <= 0) {
                return false;
            } else if (text.indexOf(".") != -1 && text.split("\\.")[1].length() > MAX_DECIMAL_PLACES) {
                return false;
            } else {
                return true;
            }
        } catch (NumberFormatException e) {
            if (".".equals(text)){
                return  true;
            } else return false;
        }
    }
}
