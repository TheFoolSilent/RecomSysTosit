package com.recomsys.demo.web;

import java.util.List;

public class Skill {
    private String wantjob;
    private List<String> skillset;

    public String getWantjob() {
        return wantjob;
    }

    public void setWantjob(String wantjob) {
        this.wantjob = wantjob;
    }

    public List<String> getSkillset() {
        return skillset;
    }

    public void setSkillset(List<String> skillset) {
        this.skillset = skillset;
    }
}
