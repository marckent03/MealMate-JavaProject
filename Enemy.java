public class Enemy extends Character {
    
    public Enemy(String name, String type, int hp, int mp) {
        super(name, type, hp, mp);
    }
    
    public void resetToMaxHealth() {
        this.currentHP = this.maxHP;
        this.currentMP = this.maxMP;
    }
    
    @Override
    public int attack() {
        reduceCooldowns();
        
        int skillChoice = (int)(Math.random() * 3) + 1;
        
        if (skillChoice == 2 && skill2Cooldown > 0) skillChoice = 1;
        if (skillChoice == 3 && skill3Cooldown > 0) skillChoice = 1;
        
        int mpCost = (skillChoice == 1) ? 0 : (skillChoice == 2) ? 20 : 40;
        if (currentMP < mpCost) skillChoice = 1;
        
        currentMP -= mpCost;
        
        if (skillChoice == 2) skill2Cooldown = 2;
        if (skillChoice == 3) skill3Cooldown = 3;
        
        System.out.println(name + " uses " + getSkillName(skillChoice) + "!");
        
        int baseDamage = getSkillDamage(skillChoice);
        return calculateDamageWithVariation(baseDamage);
    }
    
    private int calculateDamageWithVariation(int baseDamage) {
        double variation = 0.85 + (Math.random() * 0.30);
        int finalDamage = (int)(baseDamage * variation);
        
        if (variation >= 1.10) {
            System.out.println("!!! ENEMY CRITICAL HIT! !!!");
        }
        
        return finalDamage;
    }
    
    @Override
    public String getSkillName(int skillNum) {
        if (type.equals("Aries")) {
            if (skillNum == 1) return "Raging Charge";
            if (skillNum == 2) return "Blazing Strike";
            return "Infernal Cataclysm";
        } else if (type.equals("Cancer")) {
            if (skillNum == 1) return "Tidal Wave";
            if (skillNum == 2) return "Moonlit Barrier";
            return "Eclipse Embrace";
        } else if (type.equals("Sagittarius")) {
            if (skillNum == 1) return "Piercing Arrow";
            if (skillNum == 2) return "Rain of Stars";
            return "Celestial Execution";
        } else if (type.contains("God")) {
            if (skillNum == 1) return "Thunder Wrath";
            if (skillNum == 2) return "Divine Judgement";
            return "Storm Overload";
        }
        return "Attack";
    }
    
    @Override
    public int getSkillDamage(int skillNum) {
        int baseDamage = 0;
        
        if (type.equals("Aries")) {
            if (skillNum == 1) {
                baseDamage = 85 + (int)(Math.random() * 11);
            } else if (skillNum == 2) {
                baseDamage = 120 + (int)(Math.random() * 21);
            } else {
                baseDamage = 200 + (int)(Math.random() * 21);
            }
        } else if (type.equals("Cancer")) {
            if (skillNum == 1) {
                baseDamage = 75 + (int)(Math.random() * 11);
            } else if (skillNum == 2) {
                baseDamage = 100 + (int)(Math.random() * 21);
            } else {
                baseDamage = 180 + (int)(Math.random() * 21);
            }
        } else if (type.equals("Sagittarius")) {
            if (skillNum == 1) {
                baseDamage = 90 + (int)(Math.random() * 11);
            } else if (skillNum == 2) {
                baseDamage = 130 + (int)(Math.random() * 21);
            } else {
                baseDamage = 210 + (int)(Math.random() * 21);
            }
        } else if (type.equals("God_Phase1")) {
            if (skillNum == 1) {
                baseDamage = 110 + (int)(Math.random() * 21);
            } else if (skillNum == 2) {
                baseDamage = 160 + (int)(Math.random() * 21);
            } else {
                baseDamage = 240 + (int)(Math.random() * 21);
            }
        } else if (type.equals("God_Phase2")) {
            if (skillNum == 1) {
                baseDamage = 140 + (int)(Math.random() * 21);
            } else if (skillNum == 2) {
                baseDamage = 190 + (int)(Math.random() * 21);
            } else {
                baseDamage = 290 + (int)(Math.random() * 21);
            }
        } else if (type.contains("_Minion")) {
            baseDamage = 35 + (int)(Math.random() * 26);
        } else {
            baseDamage = 50;
        }
        
        return baseDamage;
    }
    
    @Override
    public String getAttackMessage() {
        if (type.equals("Aries")) {
            return name + " charges with blazing fury!";
        } else if (type.equals("Cancer")) {
            return name + " strikes with tidal force!";
        } else if (type.equals("Sagittarius")) {
            return name + " fires a starlight arrow!";
        } else if (type.contains("God")) {
            return name + " unleashes divine power!";
        } else if (type.contains("_Minion")) {
            return name + " attacks fiercely!";
        }
        return name + " attacks!";
    }
}