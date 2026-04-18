package viper.sentinox.control;

import java.io.IOException;

public interface BlueskyFeederInteface {
    void feedMedicinesFromList(String[] medicines,
                               String token,
                               String password)
            throws IOException, InterruptedException;
}
