/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rollsoftware.br.lfms.db.impl.entity;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import org.eclipse.persistence.jaxb.MarshallerProperties;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Rogério
 */
public class StockStateTest {

    public StockStateTest() {
    }

    protected <T extends Enum>
            Class<T> getEnumClass() {
        return (Class<T>) StockState.class;
    }

    protected StockState createEnum() {

        int idx = ((int) (StockState.values().length * Math.random()))
                % StockState.values().length;

        return StockState.values()[idx];
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testBasicStockState() {

        System.out.println("testBasicStockState");

        StockState stockState = createEnum();

        System.out.println("StockState: " + stockState);

        assertNotNull(stockState);
    }

    @Test
    public void testStockStateToXML() throws JAXBException {

        System.out.println("testStockStateToXML");

        JAXBContext jc = JAXBContext.newInstance(getEnumClass());

        StockState stockState = createEnum();

        Marshaller marshaller = jc.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

        System.out.println("XML Output:");
        marshaller.marshal(stockState, System.out);
        System.out.println();
    }

    @Test
    public void testStockStateToJSON() throws JAXBException {

        System.out.println("testStockStateToJSON");

        JAXBContext jc = JAXBContext.newInstance(getEnumClass());

        StockState stockState = createEnum();

        Marshaller marshaller = jc.createMarshaller();

        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.setProperty(
                MarshallerProperties.MEDIA_TYPE, "application/json");
        marshaller.setProperty(MarshallerProperties.JSON_INCLUDE_ROOT, false);

        System.out.println("JSON Output:");
        marshaller.marshal(stockState, System.out);
        System.out.println();
    }
}
