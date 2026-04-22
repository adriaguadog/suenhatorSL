package model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReservaInvitado {

    private Reserva reserva;
    private Invitado invitado;
    private boolean esConfirmado;
}