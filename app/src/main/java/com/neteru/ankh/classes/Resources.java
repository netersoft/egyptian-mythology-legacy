package com.neteru.ankh.classes;

import android.content.Context;
import android.preference.PreferenceManager;

import com.neteru.ankh.R;
import com.neteru.ankh.classes.databases.DbManager;
import com.neteru.ankh.classes.models.Quiz;

public class Resources {
    private Context context;
    private DbManager dbManager;

    private String[][] res;

    private Resources(Context ctx){
        context = ctx;
        dbManager = new DbManager(ctx);

        res = new String[][]{
                {context.getString(R.string.q1), "Osiris", "Osiris", "Anubis", "Onasis", "Thot"},
                {context.getString(R.string.q2), "Râ", "Anubis", "Osiris", "Râ", "Thot"},
                {context.getString(R.string.q3), "Le Noun", "Le Nil", "Le Noun", "Le Ren", "Le Ben"},
                {context.getString(R.string.q4), "Apophis", "Apophis", "Yormungand", "Aido-Wedo", "Waagal"},
                {context.getString(R.string.q5), "Horakhty", "Horakhty", "Mehen", "Heqa", "Douat"},
                {context.getString(R.string.q6), "La Douat", "La Douat", "Le Noun", "La Touad", "La Heqa"},
                {context.getString(R.string.q7), "Le Benben", "Le Benben", "Le Noun", "Le Horakhty", "Le Ren"},
                {context.getString(R.string.q8), "Shou", "Shou", "Seth", "Thot", "Mehen"},
                {context.getString(R.string.q9), "Tefnout", "Tefnout", "Nout", "Nephtys", "Sekhmet"},
                {context.getString(R.string.q10), "Geb", "Geb", "Nout", "Shou", "Tefnout"},
                {context.getString(R.string.q11), "Nout", "Nout", "Geb", "Isis", "Maât"},
                {context.getString(R.string.q12), "Isis", "Isis", "Nephtys", "Neith", "Selkis"},
                {context.getString(R.string.q13), "Nephtys", "Nephtys", "Isis", "Selkis", "Neith"},
                {context.getString(R.string.q14), "5", "5", "6", "2", "3"},
                {context.getString(R.string.q15), "Isis", "Isis", "Sekhmet", "Maât", "Hathor"},
                {context.getString(R.string.q16), "Khepri", "Khepri", "Atoum", "Râ", "Aton"},
                {context.getString(R.string.q17), "Râ", "Râ", "Amon", "Khepri", "Aton"},
                {context.getString(R.string.q18), "Atoum", "Atoum", "Aton", "Râ", "Khepri"},
                {context.getString(R.string.q19), "Seth", "Seth", "Apophis", "Apep", "Anubis"},
                {context.getString(R.string.q20), "Nephtys", "Nephtys", "Isis", "Hathor", "Bastet"},
                {context.getString(R.string.q21), "Anubis", "Anubis", "Thot", "Amon", "Seth"},
                {context.getString(R.string.q22), "14", "14", "21", "12", "16"},
                {context.getString(R.string.q23), "Horus", "Horus", "Beerus", "Thot", "Atoum"},
                {context.getString(R.string.q24), "Ptah", "Ptah", "Min", "Bès", "Khonsou"},
                {context.getString(R.string.q25), "Heqa", "Heqa", "Sia", "Hou", "Oupouaout"},
                {context.getString(R.string.q26), "Sia", "Sia", "Heqa", "Hou", "Oupouaout"},
                {context.getString(R.string.q27), "Hou", "Hou", "Sia", "Heqa", "Oupouaout"},
                {context.getString(R.string.q28), "Oupouaout", "Oupouaout", "Hou", "Sia", "Heqa"},
                {context.getString(R.string.q29), "12h", "12h", "6h", "8h", "4h"},
                {context.getString(R.string.q30), "Mehen", "Mehen", "Apophis", "Apep", "Yormungand"},
                {context.getString(R.string.q31), "Isis", "Isis", "Maât", "Ouadjet", "Nephtys"},
                {context.getString(R.string.q32), "Mout", "Mout", "Ouadjet", "Bastet", "Sekhmet"},
                {context.getString(R.string.q33), "Atoum", "Atoum", "Amon", "Aton", "Osiris"},
                {context.getString(R.string.q34), context.getString(R.string.q34_r0), context.getString(R.string.q34_r0), context.getString(R.string.q34_r1), context.getString(R.string.q34_r2), context.getString(R.string.q34_r3)},
                {context.getString(R.string.q35), "Nout", "Nout", "Shou", "Heqa", "Hathor"},
                {context.getString(R.string.q36), "Osiris", "Osiris", "Beerus", "Thot", "Anubis"},
                {context.getString(R.string.q37), "Thot", "Thot", "Anubis", "Osiris", "Ptah"},
                {context.getString(R.string.q38), "Ankh", "Ankh", "Ouadjet", "Horakhty", "Douat"},
                {context.getString(R.string.q39), "Osiris", "Osiris", "Anubis", "Hakai", "Horus"},
                {context.getString(R.string.q40), context.getString(R.string.q40_r0), context.getString(R.string.q40_r0), context.getString(R.string.q40_r1), context.getString(R.string.q40_r2), context.getString(R.string.q40_r3)},
                {context.getString(R.string.q41), context.getString(R.string.q41_r0), context.getString(R.string.q41_r0), context.getString(R.string.q41_r1), context.getString(R.string.q41_r2), context.getString(R.string.q41_r3)},
                {context.getString(R.string.q42), context.getString(R.string.q42_r0), context.getString(R.string.q42_r0), context.getString(R.string.q42_r1), context.getString(R.string.q42_r2), context.getString(R.string.q42_r3)},
                {context.getString(R.string.q43), "Héliopolis", "Héliopolis", "Hermopolis", "Thèbes", "Syène"},
                {context.getString(R.string.q44), context.getString(R.string.q44_r0), context.getString(R.string.q44_r0), context.getString(R.string.q44_r1), context.getString(R.string.q44_r2), context.getString(R.string.q44_r3)},
                {context.getString(R.string.q45), context.getString(R.string.q45_r0), context.getString(R.string.q45_r0), context.getString(R.string.q45_r1), context.getString(R.string.q45_r2), context.getString(R.string.q45_r3)},
                {context.getString(R.string.q46), context.getString(R.string.q46_r0), context.getString(R.string.q46_r0), context.getString(R.string.q46_r1), context.getString(R.string.q46_r2), context.getString(R.string.q46_r3)},
                {context.getString(R.string.q47), context.getString(R.string.q47_r0), context.getString(R.string.q47_r0), context.getString(R.string.q47_r1), context.getString(R.string.q47_r2), context.getString(R.string.q47_r3)},
                {context.getString(R.string.q48), context.getString(R.string.q48_r0), context.getString(R.string.q48_r0), context.getString(R.string.q48_r1), context.getString(R.string.q48_r2), context.getString(R.string.q48_r3)},
                {context.getString(R.string.q49), context.getString(R.string.q49_r0), context.getString(R.string.q49_r0), context.getString(R.string.q49_r1), context.getString(R.string.q49_r2), context.getString(R.string.q49_r3)},
                {context.getString(R.string.q50), "Khepri", "Khepri", "Atoum", "Bès", "Nout"},
                {context.getString(R.string.q51), "Maât", "Maât", "Isis", "Sekhmet", "Hathor"},
                {context.getString(R.string.q52), "Amémet", "Amémet", "Caulifla", "Sekhmet", "Taouret"},
                {context.getString(R.string.q53), "Sobek", "Sobek", "Isis", "Champa", "Bès"},
                {context.getString(R.string.q54), "Taouret", "Taouret", "Amémet", "Sekhmet", "Bastet"},
                {context.getString(R.string.q55), "Ouadjet", "Ouadjet", "Maât", "Hathor", "Isis"},
                {context.getString(R.string.q56), "Râ", "Râ", "Osiris", "Aton", "Ptah"},
                {context.getString(R.string.q57), "Horus", "Horus", "Râ", "Thot", "Seth"},
                {context.getString(R.string.q58), "Ptah", "Ptah", "Aton", "Amon", "Hakaishin"},
                {context.getString(R.string.q59), "Hathor", "Hathor", "Isis", "Nephtys", "Bastet"},
                {context.getString(R.string.q60), "Anubis", "Anubis", "Osiris", "Seth", "Horus"},
                {context.getString(R.string.q61), "Khepri", "Khepri", "Atoum", "Râ", "Thot"},
                {context.getString(R.string.q62), context.getString(R.string.q62_r0), context.getString(R.string.q62_r0), context.getString(R.string.q62_r1), context.getString(R.string.q62_r2), context.getString(R.string.q62_r3)},
                {context.getString(R.string.q63), context.getString(R.string.q63_r0), context.getString(R.string.q63_r0), context.getString(R.string.q63_r1), context.getString(R.string.q63_r2), context.getString(R.string.q63_r3)},
                {context.getString(R.string.q64), "Chou", "Chou", "Nout", "Geb", "Atoum"},
                {context.getString(R.string.q65), "8", "8", "9", "12", "6"},
                {context.getString(R.string.q66), "4", "4", "8", "2", "6"},
                {context.getString(R.string.q67), context.getString(R.string.q67_r0), context.getString(R.string.q67_r0), context.getString(R.string.q67_r1), context.getString(R.string.q67_r2), context.getString(R.string.q67_r3)},
                {context.getString(R.string.q68), context.getString(R.string.q68_r0), context.getString(R.string.q68_r0), context.getString(R.string.q68_r1), context.getString(R.string.q68_r2), context.getString(R.string.q68_r3)},
                {context.getString(R.string.q69), context.getString(R.string.q69_r0), context.getString(R.string.q69_r0), context.getString(R.string.q69_r1), context.getString(R.string.q69_r2), context.getString(R.string.q69_r3)},
                {context.getString(R.string.q70), context.getString(R.string.q70_r0), context.getString(R.string.q70_r0), context.getString(R.string.q70_r1), context.getString(R.string.q70_r2), context.getString(R.string.q70_r3)},
                {context.getString(R.string.q71), context.getString(R.string.q71_r0), context.getString(R.string.q71_r0), context.getString(R.string.q71_r1), context.getString(R.string.q71_r2), context.getString(R.string.q71_r3)},
                {context.getString(R.string.q72), context.getString(R.string.q72_r0), context.getString(R.string.q72_r0), context.getString(R.string.q72_r1), context.getString(R.string.q72_r2), context.getString(R.string.q72_r3)},
                {context.getString(R.string.q73), "Oudjat", "Oudjat", "Khnoum", "Ankh", "Nekhbet"},
                {context.getString(R.string.q74), "Sekhmet", "Sekhmet", "Bastet", "Hathor", "Selkis"},
                {context.getString(R.string.q75), context.getString(R.string.q75_r0), context.getString(R.string.q75_r0), context.getString(R.string.q75_r1), context.getString(R.string.q75_r2), context.getString(R.string.q75_r3)},
                {context.getString(R.string.q76), "Harmakhis", "Harmakhis", "Khepri", "Atoum", "Râ"},
                {context.getString(R.string.q77), context.getString(R.string.q77_r0), context.getString(R.string.q77_r0), context.getString(R.string.q77_r1), context.getString(R.string.q77_r2), context.getString(R.string.q77_r3)},
                {context.getString(R.string.q78), "Thot", "Thot", "Anubis", "Apep", "Khnoum"},
                {context.getString(R.string.q79), context.getString(R.string.q79_r0), context.getString(R.string.q79_r0), context.getString(R.string.q79_r1), context.getString(R.string.q79_r2), context.getString(R.string.q79_r3)},
                {context.getString(R.string.q80), "Uraeus", "Uraeus", "Nekhbet", "Ankh", "Douat"},
                {context.getString(R.string.q81), context.getString(R.string.q81_r0), context.getString(R.string.q81_r0), context.getString(R.string.q81_r1), context.getString(R.string.q81_r2), context.getString(R.string.q81_r3)},
                {context.getString(R.string.q82), "Akh", "Akh", "Sphinx", "Khnoum", "Khonsou"},
                {context.getString(R.string.q83), context.getString(R.string.q83_r0), context.getString(R.string.q83_r0), context.getString(R.string.q83_r1), context.getString(R.string.q83_r2), context.getString(R.string.q83_r3)},
                {context.getString(R.string.q84), context.getString(R.string.q84_r0), context.getString(R.string.q84_r0), context.getString(R.string.q84_r1), context.getString(R.string.q84_r2), context.getString(R.string.q84_r3)},
                {context.getString(R.string.q85), context.getString(R.string.q85_r0), context.getString(R.string.q85_r0), context.getString(R.string.q85_r1), context.getString(R.string.q85_r2), context.getString(R.string.q85_r3)},
                {context.getString(R.string.q86), "Bès", "Bès", "Hâpî", "Thot", "Khnoum"},
                {context.getString(R.string.q87), "Hâpî", "Hâpî", "Apophis", "Neith", "Min"},
                {context.getString(R.string.q88), "Khonsou", "Khonsou", "Min", "Khnoum", "Hâpî"},
                {context.getString(R.string.q89), "Min", "Min", "Khonsou", "Hâpî", "Apep"},
                {context.getString(R.string.q90), "Nekhbet", "Nekhbet", "Maât", "Douat", "Selkis"},
                {context.getString(R.string.q91), "Rê", "Rê", "Osiris", "Thot", "Horus"},
                {context.getString(R.string.q92), "Selkis", "Selkis", "Hathor", "Bastet", "Sekhmet"},
                {context.getString(R.string.q93), "Touèris", "Touèris", "Nephtys", "Isis", "Neith"},
                {context.getString(R.string.q94), "Neith", "Neith", "Sekhmet", "Hathor", "Maât"},
                {context.getString(R.string.q95), "Isis", "Isis", "Nephtys", "Neith", "Selkis"},
                {context.getString(R.string.q96), "Thot", "Thot", "Anubis", "Kaio", "Hâpî"},
                {context.getString(R.string.q97), "Horus", "Horus", "Osiris", "Amon", "Ptah"},
                {context.getString(R.string.q98), "Khnoum", "Khnoum", "Hathor", "Anubis", "Min"},
                {context.getString(R.string.q99), context.getString(R.string.q99_r0), context.getString(R.string.q99_r0), context.getString(R.string.q99_r1), context.getString(R.string.q99_r2), context.getString(R.string.q99_r3)},
                {context.getString(R.string.q100), "Bastet", "Bastet", "Sekhmet", "Isis", "Hathor"},
                {context.getString(R.string.q101), "Akhenaton", "Akhenaton", "Ramsès", "Toutankhamon", "Sethi"}
        };
    }

    public static Resources getInstance(Context ctx){
        return new Resources(ctx);
    }

    public void initQuiz(){
        int[] renegades = new int[]{34,40,41,42,44,45,46,47,48,49,62,63,67,68,69,70,71,72,75,77,79,81,83,84,85,99};
        if (PreferenceManager.getDefaultSharedPreferences(context).getString("lang", context.getString(R.string.lang)).equals("fr")){

            for (String[] re : res) {
                dbManager.db_insertQuiz(new Quiz(re[0], re[1], re[2], re[3], re[4], re[5]));
            }

        }else {

            for (int i = 0; i < res.length; i++) {
                boolean renegade = false;
                for(int j : renegades){
                    if (i == j-1){ renegade = true; }
                }

                if (renegade){
                    dbManager.db_insertQuiz(new Quiz(res[i][0], res[i][1], res[i][2], res[i][3], res[i][4], res[i][5]));
                }else {
                    dbManager.db_insertQuiz(new Quiz(res[i][0], nameFilter(res[i][1]), nameFilter(res[i][2]), nameFilter(res[i][3]), nameFilter(res[i][4]), nameFilter(res[i][5])));
                }
            }

        }
    }

    public boolean clearQuizTable(){ return dbManager.clearQuizTable(); }

    private String nameFilter(String name){
        String[][] filter = new String[][]{
                {"ou","u"},{"Ou","u"},{"â","a"},{"î","i"},{"ê","e"},{"è","e"},{"é","e"},{"Le","The"},{"Aton","Aten"},{"Amon","Amun"}
        };
        String result = name;

        for(String[] elm : filter){
            result = result.replace(elm[0], elm[1]);
        }

        return result;
    }
}
