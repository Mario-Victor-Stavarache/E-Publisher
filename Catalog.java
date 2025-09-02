package eeditura;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

class Catalog implements Serializable {
    private List<Carte> carti = new ArrayList<>();

    public void adaugaCarte(Carte carte) {
        carti.add(carte);
        salveazaInFisier("catalog.dat");
    }

    public List<Carte> getCarti() {
        return carti;
    }

    public void salveazaInFisier(String numeFisier) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(numeFisier))) {
            oos.writeObject(this);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Catalog incarcaDinFisier() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("catalog.dat"))) {
            return (Catalog) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return new Catalog();
        }
    }

    @Override
    public String toString() {
        if (carti.isEmpty()) return "The catalog is empty.\n";
        StringBuilder sb = new StringBuilder();
        int index = 1;
        for (Carte carte : carti) {
            sb.append("#").append(index++).append("\n");
            sb.append("Title: ").append(carte.getTitlu()).append("\n");
            sb.append("Authors: ").append(String.join(", ", carte.getAutori())).append("\n");
            sb.append("Publication year: ").append(carte.getAnAparitie()).append("\n");
            sb.append("Page count: ").append(carte.getNrPagini()).append("\n");
            sb.append("Price: $").append(String.format("%.2f", carte.getPret())).append("\n");
            sb.append("Delivery time: ").append(carte.getDurataLivrare()).append(" days\n");
            sb.append("Reviews: ").append(String.join(" | ", carte.getRecenzii())).append("\n");
            sb.append("------------------------------\n\n");
        }
        return sb.toString();
    }
}
