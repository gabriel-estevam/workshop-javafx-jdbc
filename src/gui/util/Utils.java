package gui.util;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.stage.Stage;

public class Utils 
{
	/*Essa classe é responsavel por pegar a tela principal e seus recursos
	 * a função CurrentStage() tem como parametro uma ActionEvent, apartir do ActionEvent
	 * pegamos todos os recursos da tela principal e fazemos dois dowscasting
	 * o primeiro para o tipo "Node" para portemos acessar a função getScene() e getWindow(), isto é,
	 * estamos pegando os recursos da tela principal. E na sequencia fazemos outro downCasting para o tipo Stage
	 * que é o palco da tela principal*/
	public static Stage currentStage(ActionEvent event)
	{
		return (Stage) ((Node)event.getSource()).getScene().getWindow();
	}
}
