/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014-2020 Ioannis Moutsatsos, Bruno P. Kinoshita
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package org.biouno.unochoice;
import org.jenkinsci.Symbol;
import org.jenkinsci.plugins.configfiles.GlobalConfigFiles;
import org.jenkinsci.lib.configprovider.model.Config;

import hudson.Extension;
import org.apache.commons.lang.StringUtils;
import org.biouno.unochoice.model.Script;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.DataBoundSetter;

import java.util.*;
import java.util.stream.Collectors;


public class ConfigFileChoiceParameter extends AbstractUnoChoiceParameter implements ScriptableParameter<Map<Object, Object>>  {

    /*
     * Serial UID.
     */
    private static final long serialVersionUID = -4449319038169585222L;

    /**
     * Choice type.
     */
    private final String choiceType;

    /**
     * Config File id.
     */
    private String configFileId;

    /**
     * Config File id.
     */
    private Integer visibleItemCount;

    /**
     * Filter flag.
     */
    private final Boolean filterable;

    /**
     * Filter length. Defines a minimum number of characters that must be entered before the filter
     * is activated.
     */
    private Integer filterLength;

    /**
     * Constructor called from Jelly with parameters.
     * @param name name
     * @param description description
     * @param choiceType choice type
     * @param filterable filter flag
     */
    @DataBoundConstructor
    public ConfigFileChoiceParameter(String name, String description, String choiceType,
                                     Boolean filterable, String configFileId) {
        super(name, description);
        this.choiceType = StringUtils.defaultIfBlank(choiceType, PARAMETER_TYPE_SINGLE_SELECT);
        this.filterable = filterable;
        this.configFileId = configFileId;
    }

    public String getConfigFileId() {
        return configFileId;
    }

    @DataBoundSetter
    public void setConfigFileId(String configFileId) {
        this.configFileId = configFileId;
        LOGGER.severe("configFileId: "+configFileId+"; ");
    }

    public Integer getVisibleItemCount() {
        return visibleItemCount;
    }

    @DataBoundSetter
    public void setVisibleItemCount(Integer visibleItemCount) {
        this.visibleItemCount = visibleItemCount;
    }

    /*
     * (non-Javadoc)
     * @see org.biouno.unochoice.AbstractUnoChoiceParameter#getChoiceType()
     */
    @Override
    public String getChoiceType() {
        return this.choiceType;
    }

    /**
     * Get the filter flag.
     * @return filter flag
     */
    public Boolean getFilterable() {
        return filterable;
    }

    /**
     * Get the filter length.
     * @return filter length
     */
    public Integer getFilterLength() {
        return filterLength == null ? (Integer) 1 : filterLength;
    }

    @DataBoundSetter
    public void setFilterLength(final Integer filterLength) {
        this.filterLength = filterLength;
    }

    @Override
    public Map<Object, Object> getChoices(Map<Object, Object> parameters) {
        Config config = GlobalConfigFiles.get().getById(configFileId);
        if (config == null) {
            LOGGER.severe("Config with id '" + configFileId + "' is null!");
            List<String> collect = GlobalConfigFiles.get().getConfigs().stream().map(Config::toString).collect(Collectors.toList());
            LOGGER.severe("Existing configs: '" + collect + "'");

        }

        String[] split = config.content.split("\n");

        Map<Object, Object> objectObjectHashMap = new LinkedHashMap<>();
        for (String s : split) {
            objectObjectHashMap.put(s,s);
        }

        return objectObjectHashMap;
    }

    public Map<Object, Object> getChoices() {
        return getChoices(Collections.emptyMap());
    }


    // --- descriptor

    @Symbol("configFileChoiceParameter")
    @Extension
    public static final class DescriptImpl extends ParameterDescriptor {

        @Override
        public String getDisplayName() {
            return "Config File Choices Parameter";
        }

    }




}
