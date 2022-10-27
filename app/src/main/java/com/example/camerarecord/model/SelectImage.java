package com.example.camerarecord.model;

public class SelectImage {
    private static boolean isSelected;
    private static boolean firstTerm;
    private static int count = 0;

    public static boolean isFirstTerm() {
        return firstTerm;
    }

    public static void setFirstTerm(boolean firstTerm) {
        SelectImage.firstTerm = firstTerm;
    }

    public static int getCount() {
        return count;
    }

    public static void setCount(int count) {
        SelectImage.count = count;
    }


    public static boolean isIsSelected() {
        return isSelected;
    }

    public static void setIsSelected(boolean isSelected) {
        SelectImage.isSelected = isSelected;
    }
}
