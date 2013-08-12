package com.siu.android.dondusang.model;

import com.siu.android.dondusang.Application;
import com.siu.android.dondusang.R;

/**
 * Created by lukas on 8/12/13.
 */
public class User {

    private Sexe mSexe;
    private Age mAge;

//    /**
//     * is the user defined or have sexe and age just default values
//     */
//    private boolean defined;

    public User() {
        this(null, null);
    }

    public User(Sexe sexe, Age age) {
        mSexe = sexe;
        mAge = age;
    }

    public Sexe getSexe() {
        return mSexe;
    }

    public void setSexe(Sexe sexe) {
        mSexe = sexe;
    }

    public Age getAge() {
        return mAge;
    }

    public void setAge(Age age) {
        mAge = age;
    }

    public static enum Sexe {
        MALE(R.string.sexe_male), FEMALE(R.string.sexe_female);

        private int id;

        private Sexe(int id) {
            this.id = id;
        }

        public String getLabel() {
            return Application.getContext().getString(id);
        }
    }

    public static enum Age {
        LESS_18(R.string.age_less_18), BETWEEN_18_AND_65(R.string.age_between_18_and_65), BETWEEN_65_AND_70(R.string.age_between_65_and_70),
        MORE_70(R.string.age_more_70);

        private int id;

        private Age(int id) {
            this.id = id;
        }

        public String getLabel() {
            return Application.getContext().getString(id);
        }
    }
}
