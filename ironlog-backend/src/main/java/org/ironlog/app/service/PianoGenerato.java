package org.ironlog.app.service;

import org.ironlog.app.model.Piatto;

import java.util.List;

public record PianoGenerato(List<VocePianoGenerata> voci, TargetMacro raggiunto, boolean vincoliSoddisfatti, Piatto piattoScelto) {
}
