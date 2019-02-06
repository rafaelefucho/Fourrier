package com.company;

public class ResultImaginary {
    double re;
    double im;
    int freq;
    double amp;
    double phase;

    public ResultImaginary(double re, double im, int freq) {
        this.re = re;
        this.im = im;
        this.freq = freq;

        amp = Math.sqrt(re*re+im*im);
        phase = Math.atan2(im,re);

    }
}
