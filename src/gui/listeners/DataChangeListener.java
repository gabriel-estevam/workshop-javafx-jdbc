package gui.listeners;

//esse interface permite escutar outro objeto a partit de um evento
public interface DataChangeListener {
	//esse interface implementa uma fun��o (evento) que sera disparado
	//quando os dados do objeto mudarem
	void onDataChanged();
}
