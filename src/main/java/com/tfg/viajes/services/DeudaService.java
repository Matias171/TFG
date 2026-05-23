package com.tfg.viajes.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tfg.viajes.entities.Deuda;
import com.tfg.viajes.entities.Gasto;
import com.tfg.viajes.entities.Participante;
import com.tfg.viajes.entities.Usuario;
import com.tfg.viajes.entities.Viaje;
import com.tfg.viajes.repository.DeudaRepository;
import com.tfg.viajes.repository.GastoRepository;
import com.tfg.viajes.repository.ParticipanteRepository;
import com.tfg.viajes.repository.ViajeRepository;

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

        Viaje viaje = viajeRepository.findById(viajeId)
            .orElseThrow(() -> new RuntimeException("Viaje no encontrado"));

        List<Participante> participantes = participanteRepository.findByViajeId(viajeId);
        List<Gasto> gastos = gastoRepository.findByViajeId(viajeId);

        // mapa para guardar cuanto ha pagado cada usuario en total
        Map<Usuario, Double> pagado = new HashMap<>();

        // ponemos a 0 a todos los participantes
        for (Participante p : participantes) {
            pagado.put(p.getUsuario(), 0.0);
        }

        // sumamos lo que ha pagado cada uno — CORREGIDO: pagador como clave
        for (Gasto g : gastos) {
            Usuario pagador = g.getPagador();
            pagado.put(pagador, pagado.getOrDefault(pagador, 0.0) + g.getCantidad());
        }

        // calculamos el total y la parte proporcional
        double totalGastado = gastos.stream().mapToDouble(Gasto::getCantidad).sum();
        double partePorPersona = totalGastado / participantes.size();

        // balance de cada participante: lo que pagó - lo que le toca
        Map<Usuario, Double> balance = new HashMap<>();
        for (Participante p : participantes) {
            Usuario u = p.getUsuario();
            balance.put(u, pagado.getOrDefault(u, 0.0) - partePorPersona);
        }

        // separamos deudores (balance negativo) y acreedores (balance positivo)
        List<Map.Entry<Usuario, Double>> deudores = new ArrayList<>();
        List<Map.Entry<Usuario, Double>> acreedores = new ArrayList<>();

        for (Map.Entry<Usuario, Double> entry : balance.entrySet()) {
            if (entry.getValue() < -0.01) {
                deudores.add(entry);
            } else if (entry.getValue() > 0.01) {
                acreedores.add(entry);
            }
        }

        // borramos deudas anteriores para recalcular desde cero
        deudaRepository.deleteAll(deudaRepository.findByViajeId(viajeId));

        // generamos las nuevas deudas mínimas
        List<Deuda> nuevasDeudas = new ArrayList<>();
        int iDeudores = 0;
        int iAcreedores = 0;

        while (iDeudores < deudores.size() && iAcreedores < acreedores.size()) {

            // CORREGIDO: iDeudores para el deudor
            Usuario deudor = deudores.get(iDeudores).getKey();
            Usuario acreedor = acreedores.get(iAcreedores).getKey();

            double deuda = Math.abs(deudores.get(iDeudores).getValue());
            double credito = acreedores.get(iAcreedores).getValue();

            double transferencia = Math.min(deuda, credito);

            Deuda nuevaDeuda = new Deuda();
            nuevaDeuda.setDeudor(deudor);
            nuevaDeuda.setAcreedor(acreedor);
            nuevaDeuda.setCantidad(Math.round(transferencia * 100.0) / 100.0);
            nuevaDeuda.setPagada(false);
            nuevaDeuda.setViaje(viaje);
            nuevasDeudas.add(deudaRepository.save(nuevaDeuda));

            // actualizamos los balances restantes
            deudores.get(iDeudores).setValue(deudores.get(iDeudores).getValue() + transferencia);
            acreedores.get(iAcreedores).setValue(acreedores.get(iAcreedores).getValue() - transferencia);

            if (Math.abs(deudores.get(iDeudores).getValue()) < 0.01) iDeudores++;
            if (Math.abs(acreedores.get(iAcreedores).getValue()) < 0.01) iAcreedores++;
        }

        return nuevasDeudas;
    }

    // OBTENER las deudas de un viaje
    public List<Deuda> obtenerDeudasDeViaje(Long viajeId) {
        return deudaRepository.findByViajeId(viajeId);
    }

    // MARCAR una deuda como pagada
    public Deuda marcarComoPagada(Long deudaId) {
        Deuda deuda = deudaRepository.findById(deudaId)
            .orElseThrow(() -> new RuntimeException("Deuda no encontrada"));
        deuda.setPagada(true);
        return deudaRepository.save(deuda);
    }
}