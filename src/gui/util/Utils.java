package gui.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.stage.Stage;

public class Utils 
{
	/*Essa classe � responsavel por pegar a tela principal e seus recursos
	 * a fun��o CurrentStage() tem como parametro uma ActionEvent, apartir do ActionEvent
	 * pegamos todos os recursos da tela principal e fazemos dois dowscasting
	 * o primeiro para o tipo "Node" para portemos acessar a fun��o getScene() e getWindow(), isto �,
	 * estamos pegando os recursos da tela principal. E na sequencia fazemos outro downCasting para o tipo Stage
	 * que � o palco da tela principal*/
	public static Stage currentStage(ActionEvent event)
	{
		return (Stage) ((Node)event.getSource()).getScene().getWindow();
	}
	
	public static Integer tryParseToInt(String str)
	{
		//esse metodo ajuda a converter o valor da caixinha txtField para inteiro
		//caso o valor da caixinha n�o seja valido, ele retona um valor nulo
		try {
			return Integer.parseInt(str);
		}
		catch(NumberFormatException e) {
			return null;
		}
	}
	
	public static <T> void formatTableColumnDate(TableColumn<T, Date> tableColumn, String format) {
		/*metodo para formatar data
		 * tem como parametro um obj do tipo TableColumn<>, esse obj carrega um tipo T
		 * (no caso sera o seller, mas poder ser qualquer classe) e um tipo Date que � 
		 * o dado que vamos formatar, e tambem tem como parametro um obj String que � 
		 * o formato que o programador quer que seja formatada.
		 * Para o funcionamento foi usado uma EXPRESS�O LAMBDA
		 * que pega o parametro TableColumn e chama a fun��o setCellFactory( dentro da fun��o,
		 * para cada registro obtido na coluna que carrega do tipo de dado Date, vamos edita-la
		 * para isso instanciamos uma classe generica TableCell<T, Date> cell - que vai ser responsavel
		 * por pegar o dado (com a data) da coluna Date. a TableCell Tambem � um EXPRESS�O LAMBDA, e � nela
		 * que intanciamos um obj do tipo SimpleDateFormat(passando como parametro, o formato que o programador
		 * informou) e na sequencia implementa uma fun��o que vai verificar se o obj da coluna esta nulo ou n�o
		 * updateItem(Date item, boolean empty) item - obj extraido da coluna, empty - bolenano 
		 * 
		 * 
		 * */
		
		tableColumn.setCellFactory(column -> {
			TableCell<T, Date> cell = new TableCell<T, Date>() {
				private SimpleDateFormat sdf = new SimpleDateFormat(format);

				@Override
				protected void updateItem(Date item, boolean empty) {
					super.updateItem(item, empty);
					if (empty) {
						setText(null); //caso o obj  Date, esteja nulo, retorna nula para a celula da coluna
					} else {
						setText(sdf.format(item)); //caso contrario, seta o texto (dado) formatado para a celuna da coluna
					}
				}
			};
			return cell; //retorna a celula formatado para o TableCell, e o TableCell por sua vez, passa para o metodo 
			//setCellFactory formatado
		});
	}

	public static <T> void formatTableColumnDouble(TableColumn<T, Double> tableColumn, int decimalPlaces) 
	{
		//metodo para formatar um numero com casas decimais, tem a mesma logica da fun��o formatTableColumnDate
		//porem com paremtros diferentes, sendo uma TableColumn Double e um valor inteiro para que o programador
		//informe quantas casas decimais ter� o dado formatado
		//LEMBRANDO QUE O DADO � RETIDADO DE UMA CELULA(TableCell cell) DA TableColumn QUE O PROGRAMADOR INFORMAR, E 
		//A QUANTIDADE DE CASAS DECIMAIS � IMPORTANTE PARA A FORMATA��O CORRETA DA INFORMA��O
		tableColumn.setCellFactory(column -> {
			TableCell<T, Double> cell = new TableCell<T, Double>() {
				
				@Override
				protected void updateItem(Double item, boolean empty) {
					super.updateItem(item, empty);
					if (empty) {
						setText(null);
					} 
					else {
					Locale.setDefault(Locale.US);//formato americano (ponto), exemplo: 2000.00
					//caso n�o queira o formato no padr�o americano, � s� comentar a linha acima, que dai
					//sera ajustado de acordo com o idioma configurado na maquina do usu�rio
						setText(String.format("%." + decimalPlaces + "f", item));
					}
				}
			};
			return cell;
		});
	}
}
