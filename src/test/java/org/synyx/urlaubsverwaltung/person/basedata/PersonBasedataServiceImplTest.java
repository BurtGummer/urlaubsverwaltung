package org.synyx.urlaubsverwaltung.person.basedata;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.synyx.urlaubsverwaltung.person.PersonId;
import org.synyx.urlaubsverwaltung.person.Person;
import org.synyx.urlaubsverwaltung.person.PersonDeletedEvent;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PersonBasedataServiceImplTest {

    private PersonBasedataServiceImpl sut;

    @Mock
    private PersonBasedataRepository personBasedataRepository;

    @BeforeEach
    void setUp() {
        sut = new PersonBasedataServiceImpl(personBasedataRepository);
    }

    @Test
    void getBasedataByPersonId() {

        final PersonBasedataEntity personBasedataEntity = new PersonBasedataEntity();
        personBasedataEntity.setPersonId(1);
        personBasedataEntity.setPersonnelNumber("1337");
        personBasedataEntity.setAdditionalInformation("Some additional Information");

        when(personBasedataRepository.findById(1)).thenReturn(Optional.of(personBasedataEntity));

        final Optional<PersonBasedata> basedata = sut.getBasedataByPersonId(1);
        assertThat(basedata).hasValueSatisfying(personBasedata -> {
            assertThat(personBasedata.getPersonId()).isEqualTo(new PersonId(1));
            assertThat(personBasedata.getPersonnelNumber()).isEqualTo("1337");
            assertThat(personBasedata.getAdditionalInformation()).isEqualTo("Some additional Information");
        });
    }

    @Test
    void update() {

        final PersonBasedata personBasedata = new PersonBasedata(new PersonId(1), "1337", "Some additional Information");
        sut.update(personBasedata);

        final ArgumentCaptor<PersonBasedataEntity> captor = ArgumentCaptor.forClass(PersonBasedataEntity.class);
        verify(personBasedataRepository).save(captor.capture());
        final PersonBasedataEntity personBasedataEntity = captor.getValue();
        assertThat(personBasedataEntity.getPersonId()).isEqualTo(1);
        assertThat(personBasedataEntity.getPersonnelNumber()).isEqualTo("1337");
        assertThat(personBasedataEntity.getAdditionalInformation()).isEqualTo("Some additional Information");
    }

    @Test
    void getBasedataByPersonIds() {
        final PersonBasedataEntity personBasedataEntity = new PersonBasedataEntity();
        personBasedataEntity.setPersonId(1);
        personBasedataEntity.setPersonnelNumber("1337");
        personBasedataEntity.setAdditionalInformation("Some additional Information");

        final PersonBasedataEntity personBasedataEntity2 = new PersonBasedataEntity();
        personBasedataEntity2.setPersonId(2);
        personBasedataEntity2.setPersonnelNumber("1887");

        when(personBasedataRepository.findAllByPersonIdIsIn(List.of(1,2))).thenReturn(List.of(personBasedataEntity, personBasedataEntity2));

        final Map<PersonId, PersonBasedata> actual = sut.getBasedataByPersonId(List.of(1, 2));

        assertThat(actual)
            .hasSize(2)
            .containsKeys(new PersonId(1), new PersonId(2));

        assertThat(actual.get(new PersonId(1))).satisfies(basedata -> {
            assertThat(basedata.getPersonId()).isEqualTo(new PersonId(1));
            assertThat(basedata.getPersonnelNumber()).isEqualTo("1337");
            assertThat(basedata.getAdditionalInformation()).isEqualTo("Some additional Information");
        });

        assertThat(actual.get(new PersonId(2))).satisfies(basedata -> {
            assertThat(basedata.getPersonId()).isEqualTo(new PersonId(2));
            assertThat(basedata.getPersonnelNumber()).isEqualTo("1887");
            assertThat(basedata.getAdditionalInformation()).isNull();
        });
    }

    @Test
    void delete() {
        final Person person = new Person();
        sut.delete(new PersonDeletedEvent(person));

        verify(personBasedataRepository).deleteByPerson(person);
    }

    @Test
    void ensureGetBasedataForPersonIdsDoesNotIncludeEntriesWhenThereIsNoBasedata() {

        when(personBasedataRepository.findAllByPersonIdIsIn(List.of(1, 2))).thenReturn(List.of());

        final Map<PersonId, PersonBasedata> actual = sut.getBasedataByPersonId(List.of(1, 2));
        assertThat(actual).isEmpty();
    }
}
