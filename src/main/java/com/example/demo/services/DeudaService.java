package com.example.demo.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entities.Deuda;
import com.example.demo.entities.Gasto;
import com.example.demo.entities.Participante;
import com.example.demo.entities.Usuario;
import com.example.demo.entities.Viaje;
import com.example.demo.repository.DeudaRepository;
import com.example.demo.repository.GastoRepository;
import com.example.demo.repository.ParticipanteRepository;
import com.example.demo.repository.ViajeRepository;

@Service
public class DeudaService {

	@Autowired
	private DeudaRepository deudaRepository;
	
	@Autowired
	private GastoRepository gastoRepository;
	
	@Autowired
	private ParticipanteRepository participanteRepository;
	
	@Autowired
	private ViajeRepository viajeRepository;
	
	// CALCULAR y guardar las deudas de un viaje
	public List<Deuda> calcularDeudas(Long viajeId) {
		
		Viaje viaje = viajeRepository.findById(viajeId).orElseThrow(() -> new RuntimeException("Viaje no encontrado"));
		
		// Obtenemos todos los participantes y gastos del viaje
		List<Participante> participantes = participanteRepository.findByViajeId(viajeId);
		List<Gasto> gastos = gastoRepository.findByViajeId(viajeId);
		
		// mapa para guardar cuanto ha pagado cada usuario en total
		Map<Usuario, Double> pagado = new HashMap<>();
		
		// ponemos a 0 a todos los participantes
		for (Participante p : participantes) {
			pagado.put(p.getUsuario(), 0.0);
		}
		
		// sumamos lo que ha pagado cada uno
		for (Gasto g : gastos) {
			Usuario pagador = g.getPagador();
			// getOirDefault -> si no esta en el mapa devuelve 0.0
			pagado.put(pagador, pagado.getOrDefault(pagado, 0.0) + g.getCantidad());
		}
		
		// Calculamos el gasto total y lo que deberia pagar cada uno
		// el flujo seria: creamos un canal con el stream -> de cada objeto Gasto obtenemos su cantidad y lo convertimos a double
		// con el mapToDouble y sumamos los valores
		double totalGastado = gastos.stream().mapToDouble(Gasto::getCantidad).sum();
		
		// la parte proporcional que le toca a cada uno
		double partePorPersona = totalGastado / participantes.size();
		
		// mapa para ver si alguien debe dinero (negativo) o le deben dinero (positivo)
		Map<Usuario, Double> balance = new HashMap<>();
		for (Participante p : participantes) {
			Usuario u = p.getUsuario();
			// lo que pago - lo que le toca = su balance
			balance.put(u, pagado.getOrDefault(u, 0.0) - partePorPersona);
		}
		
		// LIQUIDACION
		// separamos en dos listas, los que deben (balance negativo) y los que reciben (balance positivo)
		List<Map.Entry<Usuario, Double>> deudores = new ArrayList<>();
		List<Map.Entry<Usuario, Double>> acreedores = new ArrayList<>();
		
		for (Map.Entry<Usuario, Double> entry : balance.entrySet()) {
			if (entry.getValue() < -0.01) {
				// debe dinero (margen de 1 centimo)
				deudores.add(entry);
			} else if (entry.getValue() > 0.01) {
				// le deben dinero
				acreedores.add(entry);
			}
		}
		
		// borramos las deudas anteriores de este viaje para recalcular desde cero
		List<Deuda> deudasAnteriores = deudaRepository.findByViajeId(viajeId);
		deudaRepository.deleteAll(deudasAnteriores);
		
		// generamos las nuevas deudas minimas
		List<Deuda> nuevasDeudas = new ArrayList<>();
		
		int iDeudores = 0;
		int iAcreedores = 0;
		
		while (iDeudores < deudores.size() && iAcreedores < acreedores.size()) {
			Usuario deudor = deudores.get(iAcreedores).getKey();
			Usuario acreedor = acreedores.get(iAcreedores).getKey();
			
			double deuda = Math.abs(deudores.get(iDeudores).getValue());
			double credito = acreedores.get(iAcreedores).getValue();
			
			// la transferencia es el minimo entre lo que debe y lo que le deben
			double transferencia = Math.min(deuda, credito);
			
			// creamos la deuda en la base de datos
			Deuda nuevaDeuda = new Deuda();
			nuevaDeuda.setDeudor(deudor);
			nuevaDeuda.setAcreedor(acreedor);
			nuevaDeuda.setCantidad(Math.round(transferencia * 100.0) / 100.0); // redondeo de 2 decimales
			nuevaDeuda.setPagada(false);
			nuevaDeuda.setViaje(viaje);
			nuevasDeudas.add(deudaRepository.save(nuevaDeuda));
			
			// actualizamos
			deudores.get(iDeudores).setValue(deudores.get(iDeudores).getValue() + transferencia);
			acreedores.get(iAcreedores).setValue(acreedores.get(iAcreedores).getValue() - transferencia);
			
			// si el deudor ya saldo se deuda, pasamos al siguiente
			if (Math.abs(deudores.get(iDeudores).getValue()) < 0.01) {
				iDeudores++;
			}
			
			// si el acreedor ya cobro todo, pasamos al siguiente
			if (Math.abs(acreedores.get(iAcreedores).getValue()) < 0.01) {
				iAcreedores++;
			}
		}
		
		return nuevasDeudas;
	}
	
	// OBTENER las deudas de un viaje
	public List<Deuda> obtenerDeudasDeViaje(Long viajeId) {
		return deudaRepository.findByViajeId(viajeId);
	}
	
	// MARCAR una deuda como pagada
	public Deuda marcarComoPagada(Long deudaId) {
		Deuda deuda = deudaRepository.findById(deudaId).orElseThrow(() -> new RuntimeException("Deuda no encontrada"));
		
		deuda.setPagada(true);
		return deudaRepository.save(deuda);
	}
}
