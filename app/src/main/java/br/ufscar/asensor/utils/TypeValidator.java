/*
 * Analise se um tipo de dado � v�lido 
 * 
 * Marcelo Barbosa
 * abril, 2015
 */

// declaracao do pacote
package br.ufscar.asensor.utils;

// declaracao da classe

import org.json.JSONArray;
import org.json.JSONObject;

public class TypeValidator 
{
    // declaracao de atributos
	private String strValue;
    
    // declaracao de classes
    public TypeValidator()
    {
        this.strValue = "";       
    }   
    
    // metodos de encapsulamento
    public void setStrValue(String strValue)
    {
    	this.strValue = strValue;
    }
    
    public String getStrValue()
    {
    	return this.strValue;
    }
    
    // m�todos de processamento de dados    
    public String removeEspecialCharacteres(String value)
    {
        // remove caracteres especiais de uma string
        // declara��o de vari�veis
        String newValue = value;
        
        newValue = this.removeSubString(newValue, ".");
        newValue = this.removeSubString(newValue, ",");
        newValue = this.removeSubString(newValue, "-");
        newValue = this.removeSubString(newValue, "/");        
        newValue = this.removeSubString(newValue, "\\");
        newValue = this.removeSubString(newValue, "{");
        newValue = this.removeSubString(newValue, "}");
        newValue = this.removeSubString(newValue, "(");
        newValue = this.removeSubString(newValue, ")");
        newValue = this.removeSubString(newValue, "[");
        newValue = this.removeSubString(newValue, "]");
        newValue = this.removeSubString(newValue, "*");
        newValue = this.removeSubString(newValue, "+");
        newValue = this.removeSubString(newValue, ":");
        newValue = this.removeSubString(newValue, "?");
        newValue = this.removeSubString(newValue, "@");
        newValue = this.removeSubString(newValue, "<");
        newValue = this.removeSubString(newValue, ">");
        newValue = this.removeSubString(newValue, ";");
        newValue = this.removeSubString(newValue, "#");
        newValue = this.removeSubString(newValue, "%");
        newValue = this.removeSubString(newValue, "$");
        newValue = this.removeSubString(newValue, "&");
        newValue = this.removeSubString(newValue, "=");
        
        // retorno de valor
        return newValue;
        
    }   
    
    public String removeSubString(String value, String substring)
    {
        // remove todas as ocorr�ncias de uma substring 
        String newValue = value;
        
        while(newValue.contains(substring))
        {
            newValue = newValue.replace(substring, "");
        } 
        
        // retorno de valor
        return newValue;
    }
    
    public boolean typeEvaluate(String type, String value)
    {
        // retorna verdadeiro se o tipo de dado informado pode ser convertido corretamente.
        
        // declara��o de vari�veis
        boolean status = false;
        
        // filtra o tipo de cast a ser realizado
        switch(type)
        {
            case "boolean":
                // tenta converter para o tipo l�gico
                try
                {
                    boolean booleanttype = Boolean.parseBoolean(value);
                    status = true;
                }catch(Exception e)
                     {
                         status = false;
                     }
                
            break;
                
            case "int":
                // tenta converter para o tipo inteiro
                try
                {
                    int inttype = Integer.parseInt(value);
                    status = true;
                }catch(Exception e)
                     {
                         status = false;
                     }
                
            break;
            
            case "jsonObject":
                // tenta converter para o tipo l�gico
                try
                {
                    JSONObject jsonObj = new JSONObject(value);
                    status = true;
                }catch(Exception e)
                     {
                         status = false;
                     }
                
            break;
            
            case "jsonArray":
                // tenta converter para o tipo l�gico
                try
                {
                    JSONArray jsonArray = new JSONArray(value);
                    status = true;
                }catch(Exception e)
                     {
                         status = false;
                     }
                
            break;
                
            case "long":
                // tenta converter para o tipo inteiro longo
                try
                {
                    long longtype = Long.parseLong(value);
                    status = true;
                }catch(Exception e)
                     {
                         status = false;
                     }
                
            break;
                
            case "float":
                // tenta converter para o tipo real
                try
                {
                    float floattype = Float.parseFloat(value);
                    status = true;
                }catch(Exception e)
                     {
                         status = false;
                     }
            break;
                
            case "double":
                // tenta converter para o tipo double 
                try
                {
                    double doubletype = Double.parseDouble(value);
                    
                    if(doubletype >= 0)
                    {    
                        status = true;
                    }else
                        {
                            status = false;
                        }
                    
                }catch(Exception e)
                     {
                         status = false;
                     }
                
            break;
                
            case "int+":
                // tenta converter para o tipo inteiro positivo
                try
                {
                    int inttype = Integer.parseInt(value);
                    
                    if(inttype >= 0)
                    {    
                        status = true;
                    }else
                        {
                            status = false;
                        }
                    
                }catch(Exception e)
                     {
                         status = false;
                     }
                
            break;
                
             case "long+":
                // tenta converter para o tipo inteiro longo positivo
                try
                {
                    long longtype = Long.parseLong(value);
                    
                    if(longtype >= 0)
                    {    
                        status = true;
                    }else
                        {
                            status = false;
                        }
                    
                }catch(Exception e)
                     {
                         status = false;
                     }
                
            break;
                
            case "float+":
                // tenta converter para o tipo real positivo
                try
                {
                    float floattype = Float.parseFloat(value);
                    
                    if(floattype >= 0)
                    {    
                        status = true;
                    }else
                        {
                            status = false;
                        }
                    
                }catch(Exception e)
                     {
                         status = false;
                     }
                
            break;
                
            case "double+":
                // tenta converter para o tipo double positivo
                try
                {
                    double doubletype = Double.parseDouble(value);
                    
                    if(doubletype >= 0)
                    {    
                        status = true;
                    }else
                        {
                            status = false;
                        }
                    
                }catch(Exception e)
                     {
                         status = false;
                     }
                
            break;
        }
        
        // retorno de valor
        return status;
        
    } 
    
    public boolean typeEvaluate(String type)
    {
        // retorna verdadeiro se o tipo de dado informado pode ser convertido corretamente.
        
        // declaracao de variaveis
        boolean status = false;
        
        // filtra o tipo de cast a ser realizado
        switch(type)
        {
             case "boolean":
                // tenta converter para o tipo l�gico
                try
                {
                    boolean booleanttype = Boolean.parseBoolean(this.getStrValue());
                    status = true;
                }catch(Exception e)
                     {
                         status = false;
                     }
                
            break;
            
            case "int":
                // tenta converter para o tipo inteiro
                try
                {
                    int inttype = Integer.parseInt(this.getStrValue());
                    status = true;
                }catch(Exception e)
                     {
                         status = false;
                     }
                
            break;
                
             case "long":
                // tenta converter para o tipo inteiro longo
                try
                {
                    long longtype = Long.parseLong(this.getStrValue());
                    status = true;
                }catch(Exception e)
                     {
                         status = false;
                     }
                
            break;
                
            case "float":
                // tenta converter para o tipo real
                try
                {
                    float floattype = Float.parseFloat(this.getStrValue());
                    status = true;
                }catch(Exception e)
                     {
                         status = false;
                     }
            break;
                
            case "double":
                // tenta converter para o tipo double 
                try
                {
                    double doubletype = Double.parseDouble(this.getStrValue());
                    
                    if(doubletype >= 0)
                    {    
                        status = true;
                    }else
                        {
                            status = false;
                        }
                    
                }catch(Exception e)
                     {
                         status = false;
                     }
                
            break;
                
            case "int+":
                // tenta converter para o tipo inteiro positivo
                try
                {
                    int inttype = Integer.parseInt(this.getStrValue());
                    
                    if(inttype >= 0)
                    {    
                        status = true;
                    }else
                        {
                            status = false;
                        }
                    
                }catch(Exception e)
                     {
                         status = false;
                     }
                
            break;
                
             case "long+":
                // tenta converter para o tipo inteiro longo positivo
                try
                {
                    long longtype = Long.parseLong(this.getStrValue());
                    
                    if(longtype >= 0)
                    {    
                        status = true;
                    }else
                        {
                            status = false;
                        }
                    
                }catch(Exception e)
                     {
                         status = false;
                     }
                
            break;
                
            case "float+":
                // tenta converter para o tipo real positivo
                try
                {
                    float floattype = Float.parseFloat(this.getStrValue());
                    
                    if(floattype >= 0)
                    {    
                        status = true;
                    }else
                        {
                            status = false;
                        }
                    
                }catch(Exception e)
                     {
                         status = false;
                     }
                
            break;
                
            case "double+":
                // tenta converter para o tipo double positivo
                try
                {
                    double doubletype = Double.parseDouble(this.getStrValue());
                    
                    if(doubletype >= 0)
                    {    
                        status = true;
                    }else
                        {
                            status = false;
                        }
                    
                }catch(Exception e)
                     {
                         status = false;
                     }
                
            break;
        }
        
        // retorno de valor
        return status;
        
    }
}
