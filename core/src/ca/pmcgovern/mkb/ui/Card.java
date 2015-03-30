package ca.pmcgovern.mkb.ui;

public class Card {

    private CharSequence text;
    private int type;
    private int id;
    private int state;
    private int posX;
    private int posY;
    
    public final CharSequence getText() {
        return text;
    }
    public final void setText(CharSequence text) {
        this.text = text;
    }
    public final int getType() {
        return type;
    }
    public final void setType(int type) {
        this.type = type;
    }
    public final int getId() {
        return id;
    }
    public final void setId(int id) {
        this.id = id;
    }
    public final int getState() {
        return state;
    }
    public final void setState(int state) {
        this.state = state;
    }
    public final int getPosX() {
        return posX;
    }
    public final void setPosX(int posX) {
        this.posX = posX;
    }
    public final int getPosY() {
        return posY;
    }
    public final void setPosY(int posY) {
        this.posY = posY;
    }
    
    
}
