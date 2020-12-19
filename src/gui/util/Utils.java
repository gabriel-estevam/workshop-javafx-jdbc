package gui.util;

import javafx.event.ActionEvent;
import javafx.scene.Node;
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
}
