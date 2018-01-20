package com.example.chuyi.masking_80;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Random;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by chuyi on 2018-01-16.
 */

public class masking {
    BigInteger p;
    BigInteger q;
    BigInteger N;
    public masking(String p,String q){
        this.p = new BigInteger(p);
        this.q = new BigInteger(q);
        this.N = this.p.multiply(this.q);
    }
    public String masking(int size){
        //size of b?? set 256 bits temporarily
        //degree of secret sharing of b?? set t equals to neighbor size temporarily
        int nb_size = size/3+1;
        int t = nb_size;
        int filesize = 100000;
        int id = new Random().nextInt(nb_size);


//        BigInteger[] neighbor = new BigInteger[nb_size];
//        for (int i = 0; i < neighbor.length; i++) {
//            neighbor[i] = genRand(N);
//        }
        //comment for not saving neighbor's value's memory
        long start = System.currentTimeMillis();
        byte[] bb = new byte[32] ;
        new Random().nextBytes(bb);

        BigInteger b = new BigInteger(bb); // covering value generating size = 256 bits

        BigInteger[] ss_b =new BigInteger[nb_size];
        for (int i = 0; i < t; i++) {
            new Random().nextBytes(bb);
            ss_b[i] = new BigInteger(bb);
        }

        //generate secret share value

        BigInteger nb_ss_b ;
        for (int i = 0; i < nb_size; i++) {
            nb_ss_b = eval(ss_b, i);
        }
        // comment for degree not sure and memory saving
        try{
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            Cipher[] c_all = new Cipher[nb_size+1];
            //KeyGenerator kg = KeyGenerator.getInstance("AES");
            for (int i = 0; i < nb_size; i++) {
                c_all[i] = Cipher.getInstance("AES/CTR/NoPadding");
                byte[] key = digest.digest(genRand(N).toByteArray());
                key = Arrays.copyOf(key, 16);

                SecretKeySpec skey = new SecretKeySpec(key, "AES");
                //SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
                c_all[i].init(Cipher.ENCRYPT_MODE, skey);
            }

            c_all[nb_size] = Cipher.getInstance("AES/CTR/NoPadding");
            byte[] key = digest.digest(b.toByteArray());
            key = Arrays.copyOf(key, 16);

            SecretKeySpec skey = new SecretKeySpec(key, "AES");
            //SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
            c_all[nb_size].init(Cipher.ENCRYPT_MODE, skey);


            for (int i = 0; i < filesize/8; i++) {
                byte[] x = new byte[8];
                new Random().nextBytes(x);
                BigInteger B_x = new BigInteger(x);

                byte[] blank = new byte[8];
                B_x = B_x.add(new BigInteger(c_all[nb_size].update(blank)));

                for (int j = 0; j < nb_size; j++) {
                    if(j>id){
                        B_x = B_x.add(new BigInteger(c_all[j].update(blank)));
                    }else if(j<id){
                        B_x = B_x.subtract(new BigInteger(c_all[j].update(blank)));
                    }
                }
            }
            long stop = System.currentTimeMillis();
            return "("+Integer.toString(size)+","+Float.toString((float) (stop-start)/1000)+" )s";


//            c_all[nb_size] = Cipher.getInstance("AES/CTR/NoPadding");
//            byte[] key = digest.digest(b.toByteArray());
//            key = Arrays.copyOf(key, 16);
//
//            SecretKeySpec skey = new SecretKeySpec(key, "AES");
//            //SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
//            c_all[nb_size].init(Cipher.ENCRYPT_MODE, skey);

            //long start = System.currentTimeMillis();


            //return "("+Integer.toString(size)+","+Long.toString(stop-start)+")";


        }catch (Exception e){
            return e.toString();
        }

    }
//    public SecretKeySpec hash(byte[] key){
//
//    }

//    public byte[] byte_bit_add(byte[] a, byte[] b) throws Exception{
//        if(a.length != b.length){
//            throw new IOException();
//        }
//        int l = a.length;
//        byte[] result = new byte[l];
//        for (int i = 0; i < ; i++) {
//            result[i] = (byte)(a[i]^b[i]);
//        }
//        return result;
//    }

    public static BigInteger genRand(BigInteger p){
        Random rand = new Random();
        BigInteger result = new BigInteger(p.bitLength(), rand);
        while( result.compareTo(p) >= 0 ) {
            result = new BigInteger(p.bitLength(), rand);
        }
        return result;
    }

    public static BigInteger eval(BigInteger[] poly, int k){
        BigInteger result = BigInteger.ZERO;
        BigInteger i = BigInteger.valueOf(k);

        for(int j = poly.length-1;j>=1;j--){
            result = result.add(poly[j].multiply(i).add(poly[j-1]));
        }

        return result;
    }
}
