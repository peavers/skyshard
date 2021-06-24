package io.skyshard.services;

import io.skyshard.domain.Target;

import java.util.List;

public interface AttackService {

    void attack(Target target);

    void attack(List<Target> targets);

}
