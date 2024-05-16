package org.bme.mit.iet.player;

import org.bme.mit.iet.field.Field;

/**
 * Szerelo osztaly. Annyiban ter el a szulo osztalytol, hogy meg tudja javitani az elromlott pumpakat es kilyuksztott csoveket.
 */
public class Plumber extends Player {

    /**
     * Szerelo konstruktora hivja a szulo osztaly konstruktorat.
     */
    public Plumber(Field startField) {
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
     * Felulirja a szulo osztalyt. Ebben az esetben szerelo megjavit egy mezot. A mezo lehet eromlott pumpa vagy kilyukasztott cso
     */
    @Override
    public void changeField() {
        this.currentField.repairField();
    }


}
