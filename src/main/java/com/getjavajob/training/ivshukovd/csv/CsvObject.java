package com.getjavajob.training.ivshukovd.csv;

import static java.util.Objects.hash;

public class CsvObject {

    private String name;
    private int number;

    public CsvObject(String name, int number) {
        this.name = name;
        this.number = number;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CsvObject csvObject = (CsvObject) o;
        return number == csvObject.number && name.equals(csvObject.name);
    }

    @Override
    public int hashCode() {
        return hash(name, number);
    }

    @Override
    public String toString() {
        return "name= " + name + ", number= " + number;
    }

}