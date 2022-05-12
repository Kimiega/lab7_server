package collection;

public enum Climate {
    TROPICAL_SAVANNA,
    HUMIDSUBTROPICAL,
    OCEANIC;

    public static boolean isClimate(String s){
        try {
            Climate.valueOf(s);
        } catch (IllegalArgumentException e) {
            return false;
        }
        return true;
    }
    public static String enumToStr(){
        StringBuilder s = new StringBuilder();
        for (Climate o: Climate.values())
            s.append(o.name()).append(" ");
        return s.toString();
    }
}
