package com.techchallenge.restauranteapp.domain.exception;

public class ViolacaoDeRegraNegocioException extends RuntimeException {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ViolacaoDeRegraNegocioException(String mensagem) {
        super(mensagem);
    }
}
