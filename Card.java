import java.io.Serializable;

/**
 * Created by nick on 21.01.17.
 */
public class Card implements Serializable{

    private String russian;
    private String charachters;
    private String pinyin;
    private String examples;


    public void setRussian(String russian1){
        russian=russian1;
    }

    public void setCharacters(String charachters1){
        charachters=charachters1;
    }

    public void setPinyin(String pinyin1){
        pinyin=pinyin1;
    }

    public void setExamples(String examples1){
        examples=examples1;
    }

    public  String getRussian(){
        return russian;
    }

    public  String getCharacters(){
        return charachters;
    }

    public  String getPinyin(){
        return pinyin;
    }

    public  String getExamples(){
        return examples;
    }
}
