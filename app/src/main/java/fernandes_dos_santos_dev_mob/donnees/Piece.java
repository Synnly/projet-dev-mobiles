package fernandes_dos_santos_dev_mob.donnees;

import android.graphics.Rect;
import fernandes_dos_santos_dev_mob.exceptions.piece.ExceptionNombreMursInvalide;
import fernandes_dos_santos_dev_mob.exceptions.piece.ExceptionPiecesReliesParPlusieursMurs;

import java.util.ArrayList;
import java.util.HashMap;

public class Piece {
    private String nomPiece;
    private int idPiece;
    private Modele modele;
    private ArrayList<Mur> listeMurs;

    /**
     * Constructeur d'une pièce
     * @param nomPiece Le nom de la pièce
     * @param modele Le modèle de la pièce
     */
    public Piece(String nomPiece, Modele modele) {
        this.nomPiece = nomPiece;
        this.modele = modele;
        this.idPiece = FabriqueIDs.getinstance().getIDPiece();
        this.listeMurs = new ArrayList<>(4);
        modele.ajouterPiece(this);
    }

    /**
     * Constructeur d'une pièce. Le nom de la pièce est généré automatiquement selon son identifiant
     * @param modele Le modèle de la pièce
     */
    public Piece(Modele modele) {
        this.modele = modele;
        this.idPiece = FabriqueIDs.getinstance().getIDPiece();
        this.nomPiece = "Piece " + this.idPiece;
        this.listeMurs = new ArrayList<>(4);
        modele.ajouterPiece(this);
    }

    /**
     * Constructeur de copie profonde d'une pièce
     * @param p La pièce à copier
     * @param m Le modèle dans laquelle sera insérée la pièce copiée
     */
    public Piece(Piece p, Modele m){
        this.nomPiece = p.nomPiece;
        this.idPiece = p.idPiece;
        this.modele = m;
        this.listeMurs = new ArrayList<>(4);
        HashMap<Integer, Porte> listePortes = new HashMap<>();
        HashMap<Integer, Mur> listeNouveauxMurs = new HashMap<>();
        ArrayList<Porte> portesDejaAjoutees = new ArrayList<>();

        // Creation des murs sans portes dans un premier temps
        for(Mur mur : p.listeMurs){
            this.listeMurs.add(new Mur(mur, this));

            // On ajoute les portes dans une liste temporaire
            for(Porte porte : mur.getListePortes()){
                listePortes.put(porte.getIdPorte(), porte);
            }
        }
        // Dictionnaire des copies des murs
        for(Mur mur : listeMurs){
            listeNouveauxMurs.put(mur.getIdMur(), mur);
        }

        // On ajoute les portes aux murs
        for(int i=0; i<listeMurs.size(); i++){
            listeMurs.get(i).setPiece(this);

            for(Porte porte : p.getListeMurs().get(i).getListePortes()){ // Parcours des portes du mur de la pièce à copier
                if(!portesDejaAjoutees.contains(porte)){ // Si la porte n'a pas déjà été ajoutée
                    Porte nouvellePorte = new Porte(listePortes.get(porte.getIdPorte())); // La porte à copie peut etre null, à vérifier
                    nouvellePorte.setMurA(listeNouveauxMurs.get(porte.getMurA().getIdMur()), new Rect(porte.getRectangle(porte.getMurA())));
                    nouvellePorte.setMurB(listeNouveauxMurs.get(porte.getMurB().getIdMur()), new Rect(porte.getRectangle(porte.getMurB())));
                    portesDejaAjoutees.add(porte);
                }
            }
        }
    }

    /**
     * Renvoie l'identifiant de la pièce
     */
    public int getIdPiece() {
        return idPiece;
    }

    /**
     * Renvoie le modèle de la pièce
     */
    public String getNomPiece() {
        return nomPiece;
    }

    /**
     * Modifie le nom de la pièce
     * @param nomPiece Le nom de la pièce
     */
    public void setNomPiece(String nomPiece) {
        this.nomPiece = nomPiece;
    }

    public Modele getModele() {
        return modele;
    }

    public ArrayList<Mur> getListeMurs() {
        return listeMurs;
    }

    /**
     * Ajoute le mur à la pièce s'il n'y a pas déjà un mur avec la même orientation, sinon remplace le mur existant
     * @param mur Le mur à ajouter
     */
    public void ajouterMur(Mur mur) {
        // On vérifie s'il y a déjà un mur avec la même orientation
        for(int i = 0; i < this.listeMurs.size(); i++) {
            if (listeMurs.get(i).getOrientation() == mur.getOrientation()) {
                this.listeMurs.set(i, mur);
                mur.setPiece(this);
                return;
            }
        }
        // S'il n'y a pas de mur avec la même orientation, on ajoute le mur
        this.listeMurs.add(mur);
        mur.setPiece(this);
    }

    /**
     * Renvoie le mur d'orientation donnée. Si aucun mur n'a cette orientation, renvoie null
     * @param orientation L'orientation du mur à renvoyer. Les valeurs possibles sont : Mur.NORD, Mur.EST, Mur.SUD, Mur.OUEST
     */
    public Mur getMur(int orientation) {
        for (Mur mur : listeMurs) {
            if (mur.getOrientation() == orientation) {
                return mur;
            }
        }
        return null;
    }

    /**
     * Verifie si la pièce est valide. Une pièce est valide si elle possède 4 murs d'orientation différents et valides. Voir Mur.valider() pour les exceptions prevenant d'un mur invalide
     * @throws ExceptionNombreMursInvalide Si la pièce ne possède pas 4 murs valides
     * @throws ExceptionPiecesReliesParPlusieursMurs Si une pièce est reliée par plusieurs murs à la pièce actuelle
     */
    public void valider() throws Exception {
        // Verification de la validité de chaque mur
        for (Mur mur : this.listeMurs) {
            mur.valider();
        }
        // Chaque mur est valide. On vérifie qu'il y a 4 murs valides
        if(listeMurs.size() != 4){
            throw new ExceptionNombreMursInvalide(nomPiece);
        }

        // On vérifie que pour chaque mur, il n'y a pas de piece qui soit reliée plusieurs fois à la pièce actuelle
        ArrayList<Piece> listePieces = new ArrayList<>();
        for(Mur mur : listeMurs){
            // Liste des pieces reliées au mur actuel
            ArrayList<Piece> listePiecesMurActuel = new ArrayList<>();
            for(Porte porte : mur.getListePortes()){
                listePiecesMurActuel.add(porte.getMurA().getPiece());
                listePiecesMurActuel.add(porte.getMurB().getPiece());
            }

            // On retire la pièce actuelle de la liste
            listePiecesMurActuel.remove(this);

            // On vérifie que chaque pièce reliée au mur actuel n'est pas reliée à la pièce actuelle par un autre mur
            for(Piece piece : listePiecesMurActuel){
                if(listePieces.contains(piece)){
                    throw new ExceptionPiecesReliesParPlusieursMurs(idPiece, piece.getIdPiece());
                }
            }

            listePieces.addAll(listePiecesMurActuel);
        }
    }
}
