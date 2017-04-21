/* Classe criada para obter datas formatadas.
 * 
 * Marcelo Barbosa,
 * maio, 2015.
 */

// declaração do pacote
package br.ufscar.asensor.utils;

// importação de pacotes
import java.text.SimpleDateFormat;
import java.util.Date;

// declaraçãoo da classe
public class TimeStamp
{
	// declaração de atributos
	private String dateFormat;
	private String timeFormat;
	
	// declaração de métodos
	public TimeStamp()
	{
            // método construtor sem parâmetros

            // inicialização de atributos
            this.dateFormat = "";
            this.timeFormat = "";
	}
        
        public TimeStamp(String dateFormat)
	{
            // método construtor com parâmetros

            // inicialização de atributos
            this.dateFormat = dateFormat;
            this.timeFormat = "";
	}
        
        public TimeStamp(String dateFormat, String timeFormat)
	{
            // método construtor com parâmetros

            // inicialização de atributos
            this.dateFormat = dateFormat;
            this.timeFormat = timeFormat;
	}
	
	// métodos de encapsulamento
	public void setDateFormat(String dateFormat)
	{
		this.dateFormat = dateFormat;
	}
	
	public String getDateFormat()
	{
		return this.dateFormat;
	}
	
	public void setTimeFormat(String timeFormat)
	{
		this.timeFormat = timeFormat;
	}
	
	public String getTimeFormat()
	{
		return this.timeFormat;
	}
	
	// métodos de processamento e transformação de dados
        public String getCurrentDate()
        {
            // obtém a data corrente
            // declaração de variáveis
            String date = "";
            SimpleDateFormat simpleDate = new SimpleDateFormat(this.getDateFormat());
            
            // obtém a data formatada
            date = simpleDate.format(new Date(System.currentTimeMillis()));
            
            // retorno de valor
            return date;
            
        }
        
        public String getCurrentDate(String format)
        {
            // obtém a data corrente em um formato especificado
            // declaração de variáveis
            String date = "";
            SimpleDateFormat simpleDate = new SimpleDateFormat(format);
            
            // obtém a data formatada
            date = simpleDate.format(new Date(System.currentTimeMillis()));
            
            // retorno de valor
            return date;
            
        }
                
        public String getCurrentDay()
        {
            // obtém o dia corrente
            // declaração de variáveis
            String date = "";
            SimpleDateFormat simpleDate = new SimpleDateFormat("dd");
            
            // obtém a data formatada
            date = simpleDate.format(new Date(System.currentTimeMillis()));
            
            // retorno de valor
            return date;
            
        }
                
        public String getCurrentMonth()
        {
            // obtém o mês corrente
            // declaração de variáveis
            String date = "";
            SimpleDateFormat simpleDate = new SimpleDateFormat("MM");
            
            // obtém a data formatada
            date = simpleDate.format(new Date(System.currentTimeMillis()));
            
            // retorno de valor
            return date;
            
        }
        
        public String getCurrentYear()
        {
            // obtém o ano corrente
            // declaração de variáveis
            String date = "";
            SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy");
            
            // obtém a data formatada
            date = simpleDate.format(new Date(System.currentTimeMillis()));
            
            // retorno de valor
            return date;
            
        }
        
        public String getCurrentTime()
        {
            // obtém o horário corrente
            // declaração de variáveis
            String time = "";
            SimpleDateFormat simpleDate = new SimpleDateFormat(this.getTimeFormat());
            
            // obtém o horário corrente formatado
            time = simpleDate.format(new Date(System.currentTimeMillis()));
            
            // retorno de valor
            return time;
        }
        
	public String getTimestamp()
	{
            // retorna um timestamp formatado

            // declaração de variáveis
            String timestamp = "";
            String date = "";
            String time = "";
            SimpleDateFormat simpleDate = new SimpleDateFormat(this.getDateFormat());

            // obtém a data corrente formatada
            date = simpleDate.format(new Date(System.currentTimeMillis()));

            // altera a mascara de formatação
            simpleDate.applyPattern(this.getTimeFormat());

            // obtém o horário corrente formatado
            time = simpleDate.format(new Date(System.currentTimeMillis()));

            // concatena data e horário para o timestamp
            timestamp = date + " - " + time; 

            // retorno de valor
            return timestamp;
	}
	
        public String toString()
        {
            // converte os dados da classe em uma string
            return "Date format: " + this.getDateFormat()
                    +"\nTime format: " + this.getTimeFormat()
                    +"\nDate: " + this.getCurrentDate()
                    +"\nTime: " + this.getCurrentTime()
                    +"\n\nTimestamp: " + this.getTimestamp();
        }
	
}
