package fernandes_dos_santos_dev_mob.construction.Utils;

import android.content.Context;
import com.fasterxml.jackson.databind.ObjectMapper;
import fernandes_dos_santos_dev_mob.donnees.Modele;

import java.io.*;
import java.util.ArrayList;

public class FilesUtils {

    /**
     * Ouvrer le fichier .json de chemin path puis le convertis en objet Modele.
     * @param context Le contexte de l'activité
     * @param path Le chemin du fichier .json
     * @return Le modele si le fichier a été trouvé et correctement lu, null sinon
     * @throws IOException Si le fichier a été trouvé mais qu'il y a eu une erreur lors de la lecture
     * @throws FileNotFoundException Si le fichier n'a pas été trouvé
     */
    public static Modele chargerModele(Context context, String path) throws IOException, FileNotFoundException {
        // Ouverture du fichier
        InputStream is = context.openFileInput(path);

        if (is != null) {
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String buffer = "";
            StringBuilder builder = new StringBuilder();

            // Lecture du fichier
            while ((buffer = br.readLine()) != null) {
                builder.append(buffer);
            }

            is.close();
            isr.close();

            // Creation du modele
            return new ObjectMapper().readValue(builder.toString(), Modele.class);
        }
        return null;
    }

    /**
     * Lis puis renvoie la liste des chemins contenus dans le fichier .txt de chemin path
     * @param context Le contexte de l'activité
     * @param path Le chemin du fichier .txt
     * @return La liste des chemins
     * @throws IOException S'il y a eu une erreur lors de la lecture du fichier
     */
    public static ArrayList<String> chargerChemins(Context context, String path) throws IOException{
        ArrayList<String> chemins = new ArrayList<>();
        InputStream is = context.openFileInput(path);

        if (is != null) {
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String buffer = "";

            while ((buffer = br.readLine()) != null) {
                chemins.add(buffer);
            }

            is.close();
        }
        return chemins;
    }

    /**
     * Écrit texte dans le fichier nomFichier dans le stockage privé de l'application
     * @param context Le contexte de l'activité
     * @param texte Le texte à écrire
     * @param nomFichier Le nom du fichier
     * @throws IOException S'il y a eu une erreur lors de l'écriture
     */
    public static void ecrireTexte(Context context, String texte, String nomFichier) throws IOException {
        OutputStreamWriter osw = new OutputStreamWriter(context.openFileOutput(nomFichier, Context.MODE_PRIVATE));
        osw.write(texte);
        osw.close();
    }
}
