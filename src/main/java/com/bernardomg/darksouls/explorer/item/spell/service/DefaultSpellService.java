
package com.bernardomg.darksouls.explorer.item.spell.service;

import java.util.Objects;
import java.util.Optional;

import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.bernardomg.darksouls.explorer.domain.Summary;
import com.bernardomg.darksouls.explorer.item.spell.domain.DtoSpell;
import com.bernardomg.darksouls.explorer.item.spell.domain.PersistentSpell;
import com.bernardomg.darksouls.explorer.item.spell.domain.Spell;
import com.bernardomg.darksouls.explorer.item.spell.repository.SpellRepository;
import com.bernardomg.pagination.model.PageIterable;
import com.bernardomg.pagination.model.Pagination;
import com.bernardomg.pagination.model.Sort;
import com.bernardomg.pagination.utils.Paginations;

@Service
public final class DefaultSpellService implements SpellService {

    private final SpellRepository repository;

    @Autowired
    public DefaultSpellService(final SpellRepository repo) {
        super();

        repository = Objects.requireNonNull(repo);
    }

    @Override
    public final PageIterable<Summary> getAll(final String school, final Pagination pagination, final Sort sort) {
        final Pageable      pageable;
        final Page<Summary> page;

        pageable = Paginations.toSpring(pagination, sort);

        if (Strings.isBlank(school)) {
            page = repository.findAllSummaries(pageable);
        } else {
            page = repository.findAllSummaries(school, pageable);
        }

        return Paginations.fromSpring(page);
    }

    @Override
    public final Optional<Spell> getOne(final Long id) {
        final Optional<PersistentSpell> read;
        final Optional<Spell>           result;
        final DtoSpell                  spell;

        read = repository.findById(id);

        if (read.isPresent()) {
            spell = new DtoSpell();

            BeanUtils.copyProperties(read.get(), spell);
            BeanUtils.copyProperties(read.get(), spell.getRequirements());

            result = Optional.of(spell);
        } else {
            result = Optional.empty();
        }

        return result;
    }

}
