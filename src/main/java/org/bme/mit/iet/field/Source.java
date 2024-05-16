package org.bme.mit.iet.field;

/**
 * Forras osztaly, oroklodik a Pumpa osztalybol. A forrasok a jatek tabla egyik vegpont tipusa. Nem tud elromlani. Vegtelen vizzel rendelkezik.
 */
public class Source extends Pump {

    /**
     * Forras osztaly konstruktora. Szulo osztaly konstruktorat hivaj
     */
    public Source() {
        super();
    }

    /**
     * Beallitja a forras kimeneti csovet. A bemenetet nem lehet megvaltoztatni.
     *
     * @param in  a beallitani kivant bemeneti cso
     * @param out a beallitani kivant kimeneti cso
     */
    @Override
    public void setInOutlet(Field in, Field out) {
        this.outlet = out;
    }

    /**
     * Mivel nem tud elromlani a forras, mindig fix mennyisegu vizet rak a kimeneti csovere.
     */
    @Override
    public void moveWater() {
        if (alreadyMovedWater) {
            return;
        }
        setAlreadyMovedWater(true);
        if (outlet == null) {
            return;
        }
        var success = outlet.addWater(1);
        if (!success) {
            return;
        }
        outlet.moveWater();
    }

    /**
     * A forrasban vegtelen mennyisegu viz van. Ha egy cso vizet visz a forrasbol, akkor mindig rendelkezesre all.
     *
     * @param amount a forrastol elvinni kivant vizmennyiseg
     * @return a kivett vizmennyiseg
     */
    @Override
    public int removeWater(int amount) {
        return amount;
    }

    /**
     * Getter az azonositohoz
     *
     * @return az azonosito
     */
    public int getId() {
        return this.id;
    }


}
