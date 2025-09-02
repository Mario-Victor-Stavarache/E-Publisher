package eeditura;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

class DataManager {

    public static <T> void salvareLista(List<T> lista, String numeFisier) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(numeFisier))) {
            oos.writeObject(lista);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> List<T> incarcareLista(String numeFisier) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(numeFisier))) {
            return (List<T>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return new ArrayList<>();
        }
    }
}