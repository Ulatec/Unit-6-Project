package com.worldbankdata.DAO;

import com.worldbankdata.model.Country;

public interface CountryDAO {
    void save(Country country);
    Country findCountryByCode(String code);
    void delete(Country country);
    void update(Country country);
    Double correlationCoefficient();
    Country getMaxInternetUsers();
    Country getMinInternetUsers();
    Country getMaxAdultLiteracy();
    Country getMinAdultLiteracy();
}
