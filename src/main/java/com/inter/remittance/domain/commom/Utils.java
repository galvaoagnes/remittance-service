package com.inter.remittance.domain.commom;

import java.util.regex.Pattern;

public class Utils {
    public static final Pattern CPF_FORMAT = Pattern.compile( "^\\d{3}\\.?\\d{3}\\.?\\d{3}-?\\d{2}$");
    public static final Pattern CNPJ_FORMAT = Pattern.compile("^\\d{2}\\.?\\d{3}\\.?\\d{3}/?\\d{4}-?\\d{2}$");
    public static final Pattern EMAIL_FORMAT = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    public static final Pattern PASSWORD_FORMAT = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&]).+$");

    public static Boolean isValidEmailFormat(String email) {
        return EMAIL_FORMAT.matcher(email).matches();
    }

    public static Boolean isValidCpfFormat(String cpf) {
        return CPF_FORMAT.matcher(cpf).matches();
    }

    public static Boolean isValidCnpjFormat(String cnpj) {
        return CNPJ_FORMAT.matcher(cnpj).matches();
    }

    public static Boolean isValidPasswordFormat(String password){
        return PASSWORD_FORMAT.matcher(password).matches();
    }

}
