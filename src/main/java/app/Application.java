package app;

import io.github.humbleui.jwm.*;
import io.github.humbleui.jwm.skija.EventFrameSkija;
import io.github.humbleui.skija.Surface;

import java.io.File;
import java.util.function.Consumer;

public class Application implements Consumer<Event> {
    /**
     * окно приложения
     */
    private final Window window;

    public Application() {
        window = App.makeWindow();
        window.setEventListener(this);
        window.setTitle("Java 2D");
        window.setWindowSize(900, 900);
        window.setWindowPosition(100, 100);
        window.setIcon(new File("src/main/resources/windows.ico"));
        String[] layerNames = new String[]{
                "LayerGLSkija", "LayerRasterSkija"
        };

        // перебираем слои
        for (String layerName : layerNames) {
            String className = "io.github.humbleui.jwm.skija." + layerName;
            try {
                Layer layer = (Layer) Class.forName(className).getDeclaredConstructor().newInstance();
                window.setLayer(layer);
                break;
            } catch (Exception e) {
                System.out.println("Ошибка создания слоя " + className);
            }
        }

        // если окну не присвоен ни один из слоёв
        if (window._layer == null)
            throw new RuntimeException("Нет доступных слоёв для создания");
        window.setVisible(true);
    }

    /**
     * Обработчик событий
     * @param e событие
     */
    @Override
    public void accept(Event e) {
        if (e instanceof EventWindowClose) {
            App.terminate();
        }else if (e instanceof EventWindowCloseRequest) {
            window.close();
        }else if (e instanceof EventFrameSkija ee) {
            // получаем поверхность рисования
            Surface s = ee.getSurface();
            // очищаем её канвас заданным цветом
            s.getCanvas().clear(Colors.APP_BACKGROUND_COLOR);
        }
    }
}
