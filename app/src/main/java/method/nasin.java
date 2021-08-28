package method;

import java.util.ArrayList;

public class nasin {
    int score[][] = new int[6][10];
    int dan[][] = new int[6][10];
    int level[] = {10, 9, 8, 7, 6, 5, 4, 3, 2};
    ArrayList<Integer[]> list = new ArrayList<Integer[]>();


    float 국어평균;
    float 영어평균;
    float 수학평균;
    float 사회평균;
    float 과학평균;


    public void addNasin(int 학기종류, int 내신등급, int 단위수, int 과목분류) {
        list.add(new Integer[]{학기종류, 내신등급, 단위수, 과목분류});
    }

    public float average_full() {
        int size = list.size();
        int sum = 0;
        int dan = 0;

        for (int i = 0; i <= size - 1; i++) {
            int a = list.get(i)[1];
            int b = list.get(i)[2];
            sum += a * b;
            dan += b;
        }
        float average = (float) sum / dan;
        return average;
    }

    public float average(int 학기) {
        int size = list.size();
        int sum = 0;
        int dan = 0;
        for (int i = 0; i <= size - 1; i++) {
            if (list.get(i)[0] == 학기) {
                int a = list.get(i)[1];
                int b = list.get(i)[2];
                sum += a * b;
                dan += b;
            }
        }
        float average = (float) sum / dan;
        return average;
    }


}