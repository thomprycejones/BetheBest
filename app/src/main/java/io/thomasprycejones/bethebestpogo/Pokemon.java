package io.thomasprycejones.bethebestpogo;

import android.graphics.drawable.Drawable;
import android.content.Context;

/**
 * Created by Thomas Pryce Jones on 18-08-2016.
 */
public class Pokemon {
    public Context context;
    public int id;
    private int egg;
    private String name;
    private String number;
    private String type1;
    private String type2;
    private String img;
    private String[] weakness;
    private int evolutionMultiplier1;
    private int evolutionMultiplier2;
    private int baseAtt;
    private int baseDef;
    private int baseSta;
    private Double[] cpMultipliers;

    public Pokemon (Context con, int id, String name, String number, String type1, String type2,
                    String img, int egg, int att,int def, int sta, String[] weakness, Double[] cpMultipliers )
    {
        this.context = con.getApplicationContext();
        this.id=id;
        this.name = name;
        this.number = number;
        this.type1 = type1;
        this.type2 = type2;
        this.img = img;
        this.egg = egg;
        this.baseAtt=att;
        this.baseDef=def;
        this.baseSta=sta;
        this.weakness = weakness;
        this.cpMultipliers = cpMultipliers;
    }

    public int getId(){return this.id;}
    public String getName(){return this.name;}
    public String getNumber(){return this.number;}
    public String getType1(){return this.type1;}
    public String getType2(){return this.type2;}
    public String[] getWeakness(){return this.weakness;}
    public int getWeaknessLenght(){return this.weakness.length;}
    public int getEgg(){return this.egg;}
    public int getBaseAtt(){return this.baseAtt;}
    public int getBaseDef(){return this.baseDef;}
    public int getBaseSta(){return this.baseSta;}

    public Double[] getCpMultipliers(){
        if (this.cpMultipliers == null){return null;}
        else{return this.cpMultipliers;}
    }

    public void setEvolutionMultiplier1(int ev){this.evolutionMultiplier1=ev;}
    public void setEvolutionMultiplier2(int ev){this.evolutionMultiplier2=ev;}

    public String getType1Url() {
        return "http://www.serebii.net/pokedex-bw/type/"+this.type1.toLowerCase()+".gif";
    }
    public String getType2Url() {
        if (this.type2 != null){return "http://www.serebii.net/pokedex-bw/type/"+this.type2.toLowerCase()+".gif";}
        else{return "";}
    }
    public String[] getWeaknessesUrl() {
        String[] url = new String [getWeaknessLenght()];
        for (int j = 0; j <getWeaknessLenght(); j++) {
            String temp = "http://www.serebii.net/pokedex-bw/type/"+this.weakness[j].toLowerCase()+".gif";
            url[j] = temp;
        }
        return url;
    }

    public int getImgAssets(){
        int draw = context.getResources().getIdentifier("p" + getNumber(),"drawable", context.getPackageName());
        return draw;
    }
    public String getImg(){
        return this.img;
    }

    public int getPokemonLayoutColor(){
        switch (this.type1)
        {
            case "Fire":  return context.getResources().getColor(R.color.fire_dark);
            case "Electric":  return context.getResources().getColor(R.color.electric_dark);
            case "Ice":  return context.getResources().getColor(R.color.ice_dark);
            case "Fighting":  return context.getResources().getColor(R.color.fighting_dark);
            case "Bug":  return context.getResources().getColor(R.color.bug_dark);
            case "Dark":  return context.getResources().getColor(R.color.dark_dark);
            case "Dragon":  return context.getResources().getColor(R.color.dragon_dark);
            case "Fairy":  return context.getResources().getColor(R.color.fairy_dark);
            case "Flying":  return context.getResources().getColor(R.color.flying_dark);
            case "Ghost":  return context.getResources().getColor(R.color.ghost_dark);
            case "Grass":  return context.getResources().getColor(R.color.grass_dark);
            case "Ground":  return context.getResources().getColor(R.color.ground_dark);
            case "Normal":  return context.getResources().getColor(R.color.normal_dark);
            case "Poison":  return context.getResources().getColor(R.color.poison_dark);
            case "Psychic":  return context.getResources().getColor(R.color.psychic_dark);
            case "Rock":  return context.getResources().getColor(R.color.rock_dark);
            case "Steel":  return context.getResources().getColor(R.color.steel_dark);
            case "Water":  return context.getResources().getColor(R.color.water_dark);
            default: return 0;
        }
    }

    public int getPokemonButtonColor(){
        switch (this.type1)
        {
            case "Fire":  return R.color.fire_primary;
            case "Electric":  return R.color.electric_primary;
            case "Ice":  return R.color.ice_primary;
            case "Fighting":  return R.color.fighting_primary;
            case "Bug":  return R.color.bug_primary;
            case "Dark":  return R.color.dark_primary;
            case "Dragon":  return R.color.dragon_primary;
            case "Fairy":  return R.color.fairy_primary;
            case "Flying":  return R.color.flying_primary;
            case "Ghost":  return R.color.ghost_primary;
            case "Grass":  return R.color.grass_primary;
            case "Ground":  return R.color.ground_primary;
            case "Normal":  return R.color.normal_primary;
            case "Poison":  return R.color.poison_primary;
            case "Psychic":  return R.color.psychic_primary;
            case "Rock":  return R.color.rock_primary;
            case "Steel":  return R.color.steel_primary;
            case "Water":  return R.color.water_primary;
            default: return 0;
        }
    }

    public int getPokemonBackgroundColor(){
        switch (this.type1)
        {
            case "Fire":  return context.getResources().getColor(R.color.fire_accent);
            case "Electric":  return context.getResources().getColor(R.color.electric_accent);
            case "Ice":  return context.getResources().getColor(R.color.ice_accent);
            case "Fighting":  return context.getResources().getColor(R.color.fighting_accent);
            case "Bug":  return context.getResources().getColor(R.color.bug_accent);
            case "Dark":  return context.getResources().getColor(R.color.dark_accent);
            case "Dragon":  return context.getResources().getColor(R.color.dragon_accent);
            case "Fairy":  return context.getResources().getColor(R.color.fairy_accent);
            case "Flying":  return context.getResources().getColor(R.color.flying_accent);
            case "Ghost":  return context.getResources().getColor(R.color.ghost_accent);
            case "Grass":  return context.getResources().getColor(R.color.grass_accent);
            case "Ground":  return context.getResources().getColor(R.color.ground_accent);
            case "Normal":  return context.getResources().getColor(R.color.normal_accent);
            case "Poison":  return context.getResources().getColor(R.color.poison_accent);
            case "Psychic":  return context.getResources().getColor(R.color.psychic_accent);
            case "Rock":  return context.getResources().getColor(R.color.rock_accent);
            case "Steel":  return context.getResources().getColor(R.color.steel_accent);
            case "Water":  return context.getResources().getColor(R.color.water_accent);
            default: return 0;
        }

    } //

    public int getImgEvolution(){
        if (this.id == 151){return 0;}
        else{
            String numberEv;
            int temp = this.id + 1;
            if(temp>=10){
                if (temp>=100){
                    numberEv = String.valueOf(temp);
                }
                else{
                    numberEv = "0"+String.valueOf(temp);
                }
            }
            else{
                numberEv = "00"+String.valueOf(temp);
            }
            int draw = context.getResources().getIdentifier("p" + numberEv,"drawable", context.getPackageName());
            return draw;
        }
    }

}
