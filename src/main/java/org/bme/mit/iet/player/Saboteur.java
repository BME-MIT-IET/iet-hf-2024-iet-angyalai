package org.bme.mit.iet.player;

import org.bme.mit.iet.field.Field;

/**
 * Szobotor osztaly. Annyiban ter el a szulo osztalytol, hogy el tud rontani csoveket ugy hogy "kilyukasztja" azokat.
 */
public class Saboteur extends Player {
    /**
     * Szabotor konstruktora hivja a szulo osztaly konstruktorat.
     */
    public Saboteur(Field startField) {
        super(startField);
        this.id = idCount++;
    }

    /**
     * Getter az azonositohoz
     *
     * @return az azonosito
     */
    public int getId() {
        return this.id;
    }

    /**
     * Felulirja a szulo osztalyt. A szabotor azt a csovet, amin all, rovid idore csuszossa tudja tenni. Meghivja a MakeSlippery fuggvenyt.
     */
    @Override
    public void changeField() {
        this.currentField.makeSlippery();
    }


}
