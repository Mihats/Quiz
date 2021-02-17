package space.tsig.quizgame;

public class QLevel1 {
    public String name;
    public String value;


    public QLevel1(){

    }
    public QLevel1( String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
