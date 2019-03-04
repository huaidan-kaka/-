package org.nci.platform.common.querymap.xml;

import java.util.Map;

import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

/**
 *动态语句块－－if语句块, 参数加前缀“＃”
 *<pre><code>
 * &lt;if test="equals(#quziqf, '1')"> statement &lt;/if>
 * </code></pre>
 * @author zhangfuxue
 *
 */
public class IFContent extends DynamicContent {
	
	private static final String EXPR_KEY = "test";
	
	private static final ExprUtils EXPRUTILS = new ExprUtils();

	@Override
	protected String getTagName() {
		return "if";
	}

	@Override
	protected boolean isIncluded(Map<String, Object> params) {
		String expr = attributes.get(EXPR_KEY);
		ExpressionParser parser = new SpelExpressionParser();
		EvaluationContext context = new StandardEvaluationContext();
		if(params != null){
			for(String p : params.keySet()){
				context.setVariable(p, params.get(p));
			}
		}
		boolean test = parser.parseExpression(expr).getValue(context, EXPRUTILS, Boolean.class);
		return test;
	}

}
