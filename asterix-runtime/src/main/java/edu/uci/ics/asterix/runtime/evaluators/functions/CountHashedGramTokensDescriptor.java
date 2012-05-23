package edu.uci.ics.asterix.runtime.evaluators.functions;


import edu.uci.ics.asterix.common.functions.FunctionConstants;
import edu.uci.ics.asterix.om.functions.IFunctionDescriptor;
import edu.uci.ics.asterix.om.functions.IFunctionDescriptorFactory;
import edu.uci.ics.asterix.om.types.ATypeTag;
import edu.uci.ics.asterix.om.types.BuiltinType;
import edu.uci.ics.asterix.runtime.evaluators.base.AbstractScalarFunctionDynamicDescriptor;
import edu.uci.ics.asterix.runtime.evaluators.common.GramTokensEvaluator;
import edu.uci.ics.fuzzyjoin.tokenizer.HashedUTF8NGramTokenFactory;
import edu.uci.ics.fuzzyjoin.tokenizer.ITokenFactory;
import edu.uci.ics.fuzzyjoin.tokenizer.NGramUTF8StringBinaryTokenizer;
import edu.uci.ics.hyracks.algebricks.common.exceptions.AlgebricksException;
import edu.uci.ics.hyracks.algebricks.core.algebra.functions.FunctionIdentifier;
import edu.uci.ics.hyracks.algebricks.runtime.base.IEvaluator;
import edu.uci.ics.hyracks.algebricks.runtime.base.IEvaluatorFactory;
import edu.uci.ics.hyracks.dataflow.common.data.accessors.IDataOutputProvider;

public class CountHashedGramTokensDescriptor extends AbstractScalarFunctionDynamicDescriptor {

    private static final long serialVersionUID = 1L;
    private final static FunctionIdentifier FID = new FunctionIdentifier(FunctionConstants.ASTERIX_NS,
            "counthashed-gram-tokens", 3, true);
    public static final IFunctionDescriptorFactory FACTORY = new IFunctionDescriptorFactory() {
        public IFunctionDescriptor createFunctionDescriptor() {
            return new CountHashedGramTokensDescriptor();
        }
    };

    @Override
    public FunctionIdentifier getIdentifier() {
        return FID;
    }

    @Override
    public IEvaluatorFactory createEvaluatorFactory(final IEvaluatorFactory[] args) throws AlgebricksException {
        return new IEvaluatorFactory() {
            private static final long serialVersionUID = 1L;

            @Override
            public IEvaluator createEvaluator(IDataOutputProvider output) throws AlgebricksException {
                ITokenFactory tokenFactory = new HashedUTF8NGramTokenFactory(ATypeTag.INT32.serialize(),
                        ATypeTag.INT32.serialize());
                NGramUTF8StringBinaryTokenizer tokenizer = new NGramUTF8StringBinaryTokenizer(3, true, false, true,
                        tokenFactory);
                return new GramTokensEvaluator(args, output, tokenizer, BuiltinType.AINT32);
            }
        };
    }

}
