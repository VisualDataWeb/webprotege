package edu.stanford.bmir.protege.web.server.crud.supplied;

import com.google.common.base.Optional;
import edu.stanford.bmir.protege.web.server.change.OntologyChangeList;
import edu.stanford.bmir.protege.web.server.crud.EntityCrudContext;
import edu.stanford.bmir.protege.web.server.crud.PrefixedNameExpander;
import edu.stanford.bmir.protege.web.server.crud.uuid.UUIDEntityCrudKitHandler;
import edu.stanford.bmir.protege.web.shared.crud.EntityCrudKitPrefixSettings;
import edu.stanford.bmir.protege.web.shared.crud.EntityShortForm;
import edu.stanford.bmir.protege.web.shared.crud.supplied.SuppliedNameSuffixSettings;
import edu.stanford.bmir.protege.web.shared.crud.supplied.WhiteSpaceTreatment;
import edu.stanford.bmir.protege.web.shared.crud.uuid.UUIDSuffixSettings;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;
import org.semanticweb.owlapi.model.EntityType;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.vocab.Namespaces;
import org.semanticweb.owlapi.vocab.OWLRDFVocabulary;
import uk.ac.manchester.cs.owl.owlapi.OWLDataFactoryImpl;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 16/04/2014
 */
@RunWith(MockitoJUnitRunner.class)
public class SuppliedNameSuffixEntityCrudKitHandlerTestCase {

    public static final String PREFIX = "http://stuff/";
    @Mock
    protected EntityCrudKitPrefixSettings prefixSettings;

    @Mock
    protected SuppliedNameSuffixSettings suffixSettings;

    @Mock
    protected EntityCrudContext crudContext;

    @Mock
    protected EntityShortForm entityShortForm;

    @Mock
    protected OWLOntology ontology;

    @Mock
    protected OntologyChangeList.Builder<OWLClass> builder;

    protected WhiteSpaceTreatment whiteSpaceTreatment = WhiteSpaceTreatment.TRANSFORM_TO_CAMEL_CASE;

    private OWLDataFactoryImpl dataFactory;

    @Before
    public void setUp() throws Exception {
        dataFactory = new OWLDataFactoryImpl();
    }

    @Test
    public void shouldCreatedExpandedPrefixName() {
        when(prefixSettings.getIRIPrefix()).thenReturn(PREFIX);
        when(suffixSettings.getWhiteSpaceTreatment()).thenReturn(whiteSpaceTreatment);
        when(entityShortForm.getShortForm()).thenReturn("owl:Thing");
        when(crudContext.getDataFactory()).thenReturn(dataFactory);
        when(crudContext.getTargetOntology()).thenReturn(ontology);
        when(crudContext.getPrefixedNameExpander()).thenReturn(PrefixedNameExpander.builder().withNamespaces(Namespaces.values()).build());
        when(crudContext.getTargetLanguage()).thenReturn(Optional.<String>absent());
        when(ontology.containsEntityInSignature(any(OWLEntity.class))).thenReturn(true);
        SuppliedNameSuffixEntityCrudKitHandler handler = new SuppliedNameSuffixEntityCrudKitHandler(prefixSettings, suffixSettings);
        OWLClass cls = handler.create(EntityType.CLASS, entityShortForm, crudContext, builder);
        assertThat(cls.getIRI(), is(equalTo(OWLRDFVocabulary.OWL_THING.getIRI())));
    }
}
